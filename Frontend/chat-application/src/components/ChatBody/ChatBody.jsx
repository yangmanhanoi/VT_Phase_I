import React, {Component, useEffect, useState} from 'react'
import './ChatBody.css'
import ChatList from "../ChatList/ChatList";
import ChatContent from "../ChatContent/ChatContent";
import UserProfile from "../UserProfile/UserProfile";
import userDetail from '../../api/user'
const ChatBody= ({messages, users}) => {
  // const [users, setUsers] = useState([])
  const [currentUser, setCurrentUser] = useState({})
  const [msgs, setMsgs] = useState([])
  useEffect(() => {
    const sortedData = messages.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
    setMsgs([...sortedData])
  }, [messages, users])
  const handleFbUserClicked = (userId) => {
    console.log(userId)
      if(userId >= 0)
        {
          setCurrentUser(users[userId])
          console.log("Current user is: " + users[userId])
        }
      
  }
  return (
    <div className="main__chatbody">
      <ChatList users={users} onUserClicked={handleFbUserClicked}/>
      <ChatContent messages={msgs} user={currentUser}/>
      <UserProfile user={currentUser}/>
    </div>
  );
}
export default ChatBody