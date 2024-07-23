import React, { Component } from "react";
import Avatar from "../ChatList/Avatar";
const ChatItem = ({animationDelay, message, image}) => {
  const appId = "311796025352965"
      return (
        <div
          style={{ animationDelay: `0.8s` }}
          className={`chat__item ${message.sender_id !== appId ? "other" : ""}`}
        >
          <div className="chat__item__content">
            <div className="chat__msg">{message.text}</div>
            <div className="chat__meta">
              <span>{Math.floor((Date.now() - new Date(message.timestamp))/60000)} mins ago</span>
              <span>Seen 1.03PM</span>
            </div>
          </div>
          <Avatar isOnline="active" image={image} />
        </div>
      );
    }
export default ChatItem