import './App.css'
import Nav from "./components/Nav/Nav";
import ChatBody from "./components/ChatBody/ChatBody";
import { useEffect, useState } from 'react';
import axios from 'axios';
import { Snackbar, Alert } from '@mui/material';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
function App() {
  const [users, setUsers] = useState([])
  const [messages, setMessages] = useState([])
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarStatus, setSnackbarStatus] = useState("success");
  const baseUrl = 'https://6037-2402-800-6105-ea49-31e5-7597-3916-74ce.ngrok-free.app/callback'

  useEffect(() => {
    const socket = new SockJS('http://localhost:9999/ws')
    const stompClient = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log('Connected')
        stompClient.subscribe('/topic/messages', (message) => {
          if(message.body)
            {
              const data = JSON.parse(message.body)
              console.log(data.ticket.messages)
              const sentUser = {
                image: data.avatar_url,
                id: data.id,
                name: data.username,
                active: false,
                isOnline: false
              }
              const isUserExists = users.some(user => user.id === sentUser.id)
              if(!isUserExists)
              {
                setUsers([...users, sentUser])
              }
              else{
                console.log(`User with id ${sentUser.id} has already existed`)
              } 
              setMessages(data.ticket.messages)
              setSnackbarMessage("Một tin nhắn vừa được gửi tới");
              setSnackbarStatus("success");
              setSnackbarOpen(true);
            }
        })
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      }
    })
    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [])

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };
  return (
    <div className="__main">
      <Nav />
      <ChatBody messages={messages} users = {users}/>
      <Snackbar
        anchorOrigin={{vertical:'top',horizontal:'right'}}
        open={snackbarOpen}
        autoHideDuration={6000}
        onClose={handleSnackbarClose}
      >
        <Alert
          onClose={handleSnackbarClose}
          severity={snackbarStatus}
          sx={{ width: "100%" }}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </div>
  );
}

export default App;
