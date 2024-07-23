import React, { Component, useState, useEffect, createRef, useRef } from 'react'
import './ChatContent.css'
import Avatar from "../ChatList/Avatar";
import ChatItem from "./ChatItem";
import '@fortawesome/fontawesome-free/css/all.min.css';
import response from '../../api/response';
import fbresponse from '../../api/fbresponse';
const ChatContent = ({ messages, user }) => {
  const appId = "311796025352965"
  const [cid, setId] = useState('');
  const counterRef = useRef(1);
  const messagesEndRef = createRef(null);
  const [msg, setMsg] = useState("")
  const [chat, setChat] = useState([])
  console.log(messages)
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };
  useEffect(() => {
    setChat(messages)
  }, [messages])
  const generateId = () => {
    // Format counter with leading zeros
    const formattedId = `response_${String(counterRef.current).padStart(3, '0')}`;
    counterRef.current++; // Increment counter for next call
    return formattedId;
  };
  useEffect(() => {
    const handleKeyDown = (e) => {
      if (e.keyCode == 13 && msg !== "") {
        const x = generateId()
        setId(x)
        const newChatItem = {
          id: x,
          payloads: [],
          recipient_id: user.id,
          sender_id: appId,
          text: msg,
          timestamp: Date.now()
        };

        const payload = {
          object: "ipcc",
          entry: [
            {
              time: Date.now(),
              id: "311796025352965",
              messaging: [
                {
                  sender: {
                    id: appId
                  },
                  recipient: {
                    id: user.id
                  },
                  timestamp: Date.now(),
                  message: {
                    mid: x,
                    text: msg
                  }
                }
              ]
            }
          ]
        }
        const pl = {
          message: {
            text: msg
          },
          messaging_type: "RESPONSE",
          recipient: {
            id: user.id
          }
        }
        console.log(pl)
        const fbResponse = fbresponse()
        fbResponse.post('', pl)
        .then(response => {
          console.log(response.data)
        }).catch(error => {
          console.error('Error ', error)
        })

        const axiosResponse = response()
        axiosResponse.post('/send', payload)
          .then(response => {
            console.log(response.data)
          }).catch(error => {
            console.error('Error ', error)
          })
        setChat([...chat, newChatItem])
        setMsg("")
        scrollToBottom()
      }
    }

    window.addEventListener("keydown", handleKeyDown);

    return () => {
      window.removeEventListener("keydown", handleKeyDown)
    }
  }, [msg, chat])

  useEffect(() => {
    scrollToBottom();
  }, [chat]);


  const onStateChange = (e) => {
    setMsg(e.target.value)
  }

  return (
    <div className="main__chatcontent">
      <div className="content__header">
        <div className="blocks">
          <div className="current-chatting-user">
            <Avatar
              isOnline="active"
              image={user.image}
            />
            <p>{user.name}</p>
          </div>
        </div>

        <div className="blocks">
          <div className="settings">
            <button className="btn-nobg">
              <i className="fa fa-cog"></i>
            </button>
          </div>
        </div>
      </div>
      <div className="content__body">
        <div className="chat__items">
          {chat.map((itm, index) => {
            return (
              <ChatItem
                animationDelay={index + 2}
                key={index}
                message={itm}
                image={itm.sender_id !== appId ? user.image : "https://res.cloudinary.com/das5netom/image/upload/v1717001137/37cee152-a23d-4be6-bff2-542f3047338b_e32cwo.jpg"}
              />
            );
          })}
          <div ref={messagesEndRef} />
        </div>
      </div>
      <div className="content__footer">
        <div className="sendNewMessage">
          <button className="addFiles">
            <i className="fa fa-plus"></i>
          </button>
          <input
            type="text"
            placeholder="Type a message here"
            onChange={onStateChange}
            value={msg}
          />
          <button className="btnSendMsg" id="sendMsgBtn">
            <i className="fa fa-paper-plane"></i>
          </button>
        </div>
      </div>
    </div>
  );
};
export default ChatContent