import React from 'react';
import {Routes, Route} from 'react-router-dom';
import Navbar from './Navbar'; // Import the Navbar component
import {useState, useEffect} from 'react';
import Cookies from 'js-cookie';

function Home() {



  return (
    <div>
      <Navbar /> {/* Render the Navbar component */}

      <div className="body">
        <p>Chào bạn đến với trường làng</p>
      </div>
    </div>
  );
}

export default Home;
