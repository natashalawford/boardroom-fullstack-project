// src/pages/Home.tsx

import React from 'react'
import { Button } from '@/components/ui/button'
import { useState } from 'react';
import { LoginPopup } from '@/pages/login/loginPopup'
import diceImage from '@/assets/dice.png'
import gameImage1 from '@/assets/games/image1.jpg'
import gameImage3 from '@/assets/games/image3.jpg'
import gameImage4 from '@/assets/games/image4.jpg'
import gameImage5 from '@/assets/games/image5.jpg'

const Home: React.FC = () => {
  const [showLoginPopup, setShowLoginPopup] = useState(false)

  return (
    <main className="mt-15 p-6 mb-15 flex flex-col items-center justify-center text-center">
    {/* Container with a border, padding, and a slight rounding of corners */}
    <div className="flex flex-col items-center max-w-2xl w-full">
        <img
          src={diceImage}
          alt="Boardroom Logo"
          className="w-32 h-auto mb-4"
        />
      <h1 className="scroll-m-20 text-4xl font-extrabold tracking-tight lg:text-5xl">
        Welcome to Boardroom
      </h1>
      <p className="leading-7 mt-4 text-gray-600">
        Your one-stop platform to explore, host, and join exciting board game events and borrow games.
        Created by ECSE 321 Group 17.
      </p>
      <h3 className='font-bold mt-4'>
        Your next game night starts here. Sign in and dive into the action!
      </h3>
      <Button className="mt-6 bg-[#30BCED] text-white hover:bg-[#00a6f4]" onClick={() => setShowLoginPopup(true)}>
          Get Started
        </Button>
      </div>

      {/* Feature Section Title */}
      <div className="mt-12 w-full max-w-6xl text-center">
        <h2 className="text-2xl font-bold mb-4">
          Get in the game - discover all the ways you can join the fun!
        </h2>

        {/* Feature Section */}
        <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 place-items-center">
          <div className="border p-4 rounded-lg max-w-sm text-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <h2 className="text-xl font-semibold">Discover & Borrow Games</h2>
            <p className="mt-2 text-sm text-gray-600">
              Explore our extensive collection of board games and borrow your favorites!
            </p>
          </div>

          <div className="border p-4 rounded-lg max-w-sm text-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <h2 className="text-xl font-semibold">Join or Host Events</h2>
            <p className="mt-2 text-sm text-gray-600">
              Find exciting events to join or host your own game nights!
            </p>
          </div>

          <div className="border p-4 rounded-lg max-w-sm text-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <h2 className="text-xl font-semibold">Leave Reviews</h2>
            <p className="mt-2 text-sm text-gray-600">
              Share your experiences and rate games and events to help others!
            </p>
          </div>
        </section>
      </div>

      {/* Suggested Games Section */}
      <section className="mt-12 w-full max-w-6xl">
        <h2 className="text-2xl font-bold mb-6 text-left">
          Suggested Games
        </h2>
        {/* Grid of 4 “Game Cards” */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {/* Card 1 */}
          <div className="border rounded-lg p-4 flex flex-col items-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <img
              src={gameImage1}
              alt="Game 1"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Catan</h3>
            <p className="text-sm text-gray-600 mt-2">
              Build, trade, and settle your way to victory in this classic strategy game.
            </p>
          </div>

          {/* Card 2 */}
          <div className="border rounded-lg p-4 flex flex-col items-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <img
              src={gameImage5}
              alt="Game 2"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Carcassonne</h3>
            <p className="text-sm text-gray-600 mt-2">
              Place tiles to create a medieval landscape and seize control of cities and roads.
            </p>
          </div>

          {/* Card 3 */}
          <div className="border rounded-lg p-4 flex flex-col items-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <img
              src={gameImage3}
              alt="Game 3"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Ticket to Ride</h3>
            <p className="text-sm text-gray-600 mt-2">
              Build train routes across the country in this family-friendly strategy title.
            </p>
          </div>

          {/* Card 4 */}
          <div className="border rounded-lg p-4 flex flex-col items-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <img
              src={gameImage4}
              alt="Game 4"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Pandemic</h3>
            <p className="text-sm text-gray-600 mt-2">
              Work together to save the world from deadly diseases in this cooperative favorite.
            </p>
          </div>
        </div>
      </section>

      {/* Upcoming Events Section */}
      <section className="mt-12 w-full max-w-6xl">
        <h2 className="text-2xl font-bold mb-6 text-left">
          Upcoming Events
        </h2>
        {/* Grid of 4 “Event Cards” */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {/* Event Card 1 */}
          <div className="border rounded-lg p-4 flex flex-col items-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <img
              src={gameImage3}
              alt="Event 1"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Monthly Meetup</h3>
            <p className="text-sm text-gray-600 mt-2">
              Join us for a variety of games and meet other enthusiasts in your area.
            </p>
          </div>

          {/* Event Card 2 */}
          <div className="border rounded-lg p-4 flex flex-col items-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <img
              src={gameImage4}
              alt="Event 2"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Strategy Sunday</h3>
            <p className="text-sm text-gray-600 mt-2">
              Dive into deeper strategy games like Twilight Imperium and Terra Mystica.
            </p>
          </div>

          {/* Event Card 3 */}
          <div className="border rounded-lg p-4 flex flex-col items-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <img
              src={gameImage1}
              alt="Event 3"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Family Fun Night</h3>
            <p className="text-sm text-gray-600 mt-2">
              Bring the kids and enjoy a night of lighthearted fun and laughter.
            </p>
          </div>

          {/* Event Card 4 */}
          <div className="border rounded-lg p-4 flex flex-col items-center shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200">
            <img
              src={gameImage5}
              alt="Event 4"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">New Releases Showcase</h3>
            <p className="text-sm text-gray-600 mt-2">
              Be the first to try the latest board games, fresh off the publishers’ shelves.
            </p>
          </div>
        </div>
      </section>

      {/* Render the Login Popup */}
      <LoginPopup isOpen={showLoginPopup} onClose={() => setShowLoginPopup(false)} />

    </main>
  )
}

export default Home
