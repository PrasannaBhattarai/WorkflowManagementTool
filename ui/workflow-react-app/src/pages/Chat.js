import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import axios from 'axios';
import './css/Chat.css';
import Project from './leader/AssignTasks';

const ChatApp = () => {
  const location = useLocation();
  const messageAreaRef = useRef(null);
  const [chats, setChats] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [username, setUsername] = useState('');
  const [messageInput, setMessageInput] = useState('');
  const [messageArea, setMessageArea] = useState([]);
  const [connecting, setConnecting] = useState(true);
  const [usernameFetched, setUsernameFetched] = useState(false);
  const [connected, setConnected] = useState(false);
  const [loading, setLoading] = useState(true); 
  const bottomRef = useRef(null);
  const [isTask, setIsTask] = useState(false);
  const [taskDescription, setTaskDescription] = useState('');

  const colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
  ];

  useEffect(() => {
    const socket = new SockJS('http://localhost:8081/ws');
    const client = new Client();
    client.webSocketFactory = () => socket;
    setStompClient(client);

    return () => {
      if (client && client.connected) {
        client.deactivate();
      }
    };
  }, []);

  useEffect(() => {
    if (messageAreaRef.current) {
      messageAreaRef.current.scrollTop = messageAreaRef.current.scrollHeight;
    }
  }, [messageArea]);



  useEffect(() => {
    const fetchTokenAndConnect = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          console.log('No token found, unable to connect to chat.');
          return;
        }

        const headers = {
          'Authorization': `Bearer ${token}`,
        };

        const response = await axios.get('http://localhost:8081/api/user/getUsername', { headers });
        setUsername(response.data.username);
        setUsernameFetched(true);

        if (stompClient) {
          stompClient.onConnect = () => {
            onConnected(response.data.username);
            setConnected(true);
          };
          stompClient.onStompError = onError;
          stompClient.activate();
        }
      } catch (error) {
        console.error('Error fetching token and connecting to chat:', error);
      }
    };

    fetchTokenAndConnect();
  }, [stompClient]);


  useEffect(() => {
    const fetchProjectChats = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          console.log('No token found, unable to fetch project chats.');
          return;
        }
  
        const projectId = new URLSearchParams(location.search).get('id');
        const headers = {
          'Authorization': `Bearer ${token}`,
        };
  
        const response = await axios.get(`http://localhost:8081/topic/project/${projectId}/chat`, { headers });
        const fetchedMessages = response.data.map(message => {
        const avatarColor = getAvatarColor(message.sender);
        return (
            <li key={message.chatDateTime} className="chat-message">
                <i style={{ backgroundColor: avatarColor }}>{message.sender[0]}</i>
              <div className="message-content">
                <span>{message.sender}</span>
                <p>{message.content}</p>
              </div>
            </li>
          );
        });
        setMessageArea(fetchedMessages);
        setLoading(false); 
      } catch (error) {
        console.error('Error fetching project chats:', error);
      }
    };
  
    fetchProjectChats();
  }, []);
  


  const onConnected = (username) => {
    const urlParams = new URLSearchParams(window.location.search);
    const projectId = new URLSearchParams(location.search).get('id');

    stompClient.subscribe(`/topic/project-${projectId}`, onMessageReceived);
    stompClient.publish({
        destination: "/app/chat.addUser",
        body: JSON.stringify({ sender: username, type: 'JOIN' })
    });
    setConnecting(false);
  };


  const onError = (error) => {
    console.log('Could not connect to WebSocket server. Error:', error);
  };

  const sendMessage = async (event) => {
    event.preventDefault();
    const messageContent = messageInput.trim();
    if (messageContent && stompClient && connected) {
      const token = localStorage.getItem('token');
      if (!token) {
        console.log('No token found, unable to send message.');
        return;
      }

      const urlParams = new URLSearchParams(window.location.search);
      const projectId = new URLSearchParams(location.search).get('id');

      const chatMessage = {
        sender: username,
        content: messageInput,
        type: 'CHAT',
        token: token, // including the token in the message payload
        text: messageInput,
        projectId: projectId
      };

      try {
        console.log(token);
        await stompClient.publish({
          destination: "/app/chat.sendMessage",
          body: JSON.stringify(chatMessage)
        });
        setMessageInput('');
      } catch (error) {
        console.error('Error sending message:', error);
      }
    }
  };


  const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);

    let messageElement;
    if (message.type === 'JOIN' || message.type === 'LEAVE') {
      messageElement = <li className="event-message">{`${message.sender} ${message.type === 'JOIN' ? 'has joined!' : 'has left!'}`}</li>;
    } else {
      const avatarColor = getAvatarColor(message.sender);
      messageElement = (
        <li className="chat-message">
          <i style={{ backgroundColor: avatarColor }}>{message.sender[0]}</i>
          <div className="message-content">
            <span>{message.sender}</span>
            <p>{message.content}</p>
          </div>
        </li>
      );
    }

    setMessageArea(prevMessages => [...prevMessages, messageElement]);
  };

  const getAvatarColor = (messageSender) => {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
      hash = 31 * hash + messageSender.charCodeAt(i);
    }
    const index = Math.abs(hash % colors.length);
    return colors[index];
  };

  // function to handle message input change
const handleMessageInputChange = (event) => {
  setMessageInput(event.target.value);
  setIsTask(false); // resets isTask flag when typing starts
};

// checks if the message is a task when message input length exceeds a certain threshold
useEffect(() => {
  const classifyMessage = async () => {
    if (messageInput.length > 50) { 
      try {
        console.log("Message Input:", messageInput);
        const response = await axios.post('http://127.0.0.1:8000/classify-text/', { text: messageInput });
        console.log("Response from server:", response.data);
        setIsTask(response.data.is_task);
      } catch (error) {
        console.error('Error classifying message:', error);
      }
    } else {
      setIsTask(false);
    }
  };

  classifyMessage();
}, [messageInput]);

const sendTaskDescription = (description) => {
  setTaskDescription(description);
  localStorage.setItem('taskDescription', description);
};

  return (
    <div className="chat-container">
      {connecting && <div className="connecting">Connecting...</div>}
      {usernameFetched ? (
        <div id="chat-page">
          {loading ? (
            <p>Loading messages...</p>
          ) : (
            <>
           {isTask && (
                <div className="task-indicator">
                    <h4>Would you like to make this message a task?</h4>
                    <button className='makeTask' onClick={() => sendTaskDescription(messageInput)}>Yes</button>
                </div>
            )}

              <ul id="messageArea" ref={messageAreaRef}>{messageArea}</ul><div ref={bottomRef} />
              <form id="messageForm" onSubmit={sendMessage}>
                <input type="text" id="message" placeholder="Type your message" value={messageInput} onChange={(e) => setMessageInput(e.target.value)} />
                <button className='send-button' type="submit">Send</button>
              </form>
              {isTask && <div>Would You Like to Make This Message A Task?</div>}
            </>
          )}
        </div>
      ) : (
        <div id="username-page" className="username-page-container">
          <p>Loading username...</p>
        </div>
      )}
    </div>
  );
};

export default ChatApp;
