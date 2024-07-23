import React, { useEffect, useState } from "react";
import "./UserProfile.css";

const UserProfile = ({user}) => {
  const [isOpen, setIsOpen] = useState(false)
  const [curUser, setCurUser] = useState({})
  useEffect(() => {
    setCurUser(user)
  },[user])
  const isEmptyObject = (obj) => {
    return Object.keys(obj).length === 0;
  };
  const handleOpenCard = (e) => {
    e.preventDefault()
    setIsOpen(!isOpen)
  }
  if(isEmptyObject(user))
    {
      return(
        <div>Please, choose an user</div>
      )
    }
    return (
      <div className="main__userprofile">
        <div className="profile__card user__profile__image">
          <div className="profile__image">
            <img src={user.image} style={{ width: '100%', height: '100%', objectFit: 'cover' }}/>
          </div>
          <h4>{user.name}</h4>
          <p>Student at PTIT</p>
        </div>
        <div className={!isOpen ? "profile__card" : "profile__card open"}>
          <div className="card__header" onClick={handleOpenCard}>
            <h4>Information</h4>
            <i className="fa fa-angle-down"></i>
          </div>
          <div className="card__content">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla
            ultrices urna a imperdiet egestas. Donec in magna quis ligula
          </div>
        </div>
      </div>
    );
}
export default UserProfile