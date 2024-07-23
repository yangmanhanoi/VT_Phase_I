import React, { Component } from "react";
import Avatar from "./Avatar";

const ChatListItems = ({name, id, key, animationDelay, active, isOnline, image, onClicked}) =>{
  const selectChat = (e) => {
    for (
      let index = 0;
      index < e.currentTarget.parentNode.children.length;
      index++
    ) {
      e.currentTarget.parentNode.children[index].classList.remove("active");
    }
    e.currentTarget.classList.add("active");
    onClicked(id);
  };

    return (
      <div
        style={{ animationDelay: `0.${animationDelay}s` }}
        onClick={selectChat}
        className={`chatlist__item ${
          active ? active : ""
        } `}
      >
        <Avatar
          image={
            image ? image : "http://placehold.it/80x80"
          }
          isOnline={isOnline}
        />

        <div className="userMeta">
          <p>{name}</p>
          <span className="activeTime">32 mins ago</span>
        </div>
      </div>
    );
  }
export default ChatListItems