// src/pages/Home.tsx

import React from 'react'
import { Button } from '@/components/ui/button'

// Temporary placeholder images (replace with your own local or imported assets)
const placeholderImg =
  'https://via.placeholder.com/200x150.png?text=Board+Game'

const Home: React.FC = () => {
  return (
    <main className="mt-20 p-6 flex flex-col items-center justify-center text-center">
    {/* Container with a border, padding, and a slight rounding of corners */}
    <div className="border border-gray-300 p-6 rounded-lg max-w-2xl w-full">
      <h1 className="scroll-m-20 text-4xl font-extrabold tracking-tight lg:text-5xl">
        Welcome to Boardroom!
      </h1>
      <p className="leading-7 mt-4 text-gray-600">
        Your one-stop platform to explore, host, and join exciting board game events.
        Created by ECSE 321 Group 17.
      </p>
      <Button className="mt-6">Get Started</Button>
    </div>

      {/* Feature Section */}
      <section className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-8">
        <div className="border p-4 rounded-lg">
          <h2 className="text-xl font-semibold">Discover Games</h2>
          <p className="mt-2 text-sm text-gray-600">
            Browse a vast collection of board games to find your next favorite.
          </p>
        </div>

        <div className="border p-4 rounded-lg">
          <h2 className="text-xl font-semibold">Join Events</h2>
          <p className="mt-2 text-sm text-gray-600">
            See upcoming events and meet like-minded gamers in your area.
          </p>
        </div>

        <div className="border p-4 rounded-lg">
          <h2 className="text-xl font-semibold">Host A Room</h2>
          <p className="mt-2 text-sm text-gray-600">
            As a game owner, create your own events for the community.
          </p>
        </div>
      </section>

      {/* Suggested Games Section */}
      <section className="mt-12 w-full max-w-6xl">
        <h2 className="text-2xl font-bold mb-6 text-left">
          Suggested Games
        </h2>
        {/* Grid of 4 “Game Cards” */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {/* Card 1 */}
          <div className="border rounded-lg p-4 flex flex-col items-center">
            <img
              src={placeholderImg}
              alt="Game 1"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Catan</h3>
            <p className="text-sm text-gray-600 mt-2">
              Build, trade, and settle your way to victory in this classic strategy game.
            </p>
          </div>

          {/* Card 2 */}
          <div className="border rounded-lg p-4 flex flex-col items-center">
            <img
              src={placeholderImg}
              alt="Game 2"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Carcassonne</h3>
            <p className="text-sm text-gray-600 mt-2">
              Place tiles to create a medieval landscape and seize control of cities and roads.
            </p>
          </div>

          {/* Card 3 */}
          <div className="border rounded-lg p-4 flex flex-col items-center">
            <img
              src={placeholderImg}
              alt="Game 3"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Ticket to Ride</h3>
            <p className="text-sm text-gray-600 mt-2">
              Build train routes across the country in this family-friendly strategy title.
            </p>
          </div>

          {/* Card 4 */}
          <div className="border rounded-lg p-4 flex flex-col items-center">
            <img
              src={placeholderImg}
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
          <div className="border rounded-lg p-4 flex flex-col items-center">
            <img
              src={placeholderImg}
              alt="Event 1"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Monthly Meetup</h3>
            <p className="text-sm text-gray-600 mt-2">
              Join us for a variety of games and meet other enthusiasts in your area.
            </p>
          </div>

          {/* Event Card 2 */}
          <div className="border rounded-lg p-4 flex flex-col items-center">
            <img
              src={placeholderImg}
              alt="Event 2"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Strategy Sunday</h3>
            <p className="text-sm text-gray-600 mt-2">
              Dive into deeper strategy games like Twilight Imperium and Terra Mystica.
            </p>
          </div>

          {/* Event Card 3 */}
          <div className="border rounded-lg p-4 flex flex-col items-center">
            <img
              src={placeholderImg}
              alt="Event 3"
              className="w-full h-auto object-cover mb-4"
            />
            <h3 className="text-lg font-semibold">Family Fun Night</h3>
            <p className="text-sm text-gray-600 mt-2">
              Bring the kids and enjoy a night of lighthearted fun and laughter.
            </p>
          </div>

          {/* Event Card 4 */}
          <div className="border rounded-lg p-4 flex flex-col items-center">
            <img
              src={placeholderImg}
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
    </main>
  )
}

export default Home
