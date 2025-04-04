import React, { useState, useEffect } from 'react';
import EventPopup from './eventPopup';
import EventCreationPopup from './eventCreationPopup'; // Import the event creation popup
import { fetchEvents, fetchRegistrationsForEvent, Event } from '../../services/eventService'; // Updated import
import { fetchBoardGames, BoardGame } from '../../services/boardGameService'; // Import fetchBoardGames and BoardGame interface
import { Button } from "@/components/ui/button";
import image1 from '../../assets/games/image1.jpg'
import image2 from '../../assets/games/image2.jpg'
import image3 from '../../assets/games/image3.jpg'
import image4 from '../../assets/games/image4.jpg'
import image5 from '../../assets/games/image5.jpg'

const gameImages: { [key: number]: string } = {
  1: image1,
  2: image2,
  3: image3,
  4: image4,
  5: image5
}

const Events: React.FC = () => {
    // State variables for managing search, filters, events, and UI state
    const [searchQuery, setSearchQuery] = useState('');
    const [events, setEvents] = useState<Event[]>([]);
    const [allEvents, setAllEvents] = useState<Event[]>([]); // Store all events separately
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [selectedEvent, setSelectedEvent] = useState<Event | null>(null);
    const [selectedGame, setSelectedGame] = useState(''); // Filter by game
    const [selectedDate, setSelectedDate] = useState(''); // Filter by date
    const [showCreatePopup, setShowCreatePopup] = useState(false); // State for create event popup
    const [boardGames, setBoardGames] = useState<BoardGame[]>([]); // State to store board games

    // Fetch events and board games from the backend
    const loadBoardGamesAndEvents = async () => {
        setLoading(true);
        setError('');
        try {
            const [fetchedBoardGames, fetchedEvents] = await Promise.all([
                fetchBoardGames(), // Fetch board games
                fetchEvents() // Fetch events
            ]);
            setBoardGames(fetchedBoardGames); // Store board games
            const today = new Date();
            const filteredData = await Promise.all(
                fetchedEvents.filter(event => new Date(event.startDateTime) >= today) // Exclude past events
                    .map(async event => {
                        const registrations = await fetchRegistrationsForEvent(event.id);
                        return registrations.length < event.maxParticipants ? event : null; // Exclude full events
                    })
            );
            const validEvents = filteredData.filter(event => event !== null);
            const sortedData = validEvents.sort((a, b) => 
                new Date(a!.startDateTime).getTime() - new Date(b!.startDateTime).getTime()
            ); // Sort events by closest date
            setAllEvents(sortedData as Event[]); // Store all events
            setEvents(sortedData as Event[]); // Update displayed events
        } catch (err: any) {
            setError(err.message || 'An error occurred');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadBoardGamesAndEvents(); // Fetch events and board games on mount
    }, []);

    // Apply filters (game, date, search query) to the events list
    const handleFiltersChange = () => {
        let filteredEvents = allEvents;

        if (selectedGame) {
            filteredEvents = filteredEvents.filter((event) =>
                event.boardGameName.toLowerCase() === selectedGame.toLowerCase()
            );
        }

        if (selectedDate) {
            filteredEvents = filteredEvents.filter((event) =>
                new Date(event.startDateTime).toLocaleDateString() === selectedDate
            );
        }

        if (searchQuery.trim()) {
            filteredEvents = filteredEvents.filter((event) =>
                event.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                event.boardGameName.toLowerCase().includes(searchQuery.toLowerCase())
            );
        }

        setEvents(filteredEvents);
    };

    // Reapply filters whenever a filter value changes
    useEffect(() => {
        handleFiltersChange();
    }, [selectedGame, selectedDate, searchQuery]);

    const handleCreateEvent = () => {
        setShowCreatePopup(true); // Show the create event popup
    };

    const handleCloseCreatePopup = async () => {
        setShowCreatePopup(false); // Close the create event popup
        await loadBoardGamesAndEvents(); // Reuse the same logic to fetch events again
    };

    // Open the popup for the selected event
    const handleCardClick = (event: Event) => {
        setSelectedEvent(event);
    };

    const handleClosePopup = () => {
        setSelectedEvent(null);
    };

    return (
        <div style={{ padding: '25px', justifyContent: 'center' }}>
            {/* Search and filter controls */}
            <div style={{ margin: '20px 0', display: 'flex', gap: '10px' }}>
                <input
                    type="text"
                    placeholder="Search For Events..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)} // Update dynamically
                    style={{
                        padding: '5px 10px',
                        borderRadius: '5px',
                        border: '1px solid #ccc',
                        width: '300px',
                    }}
                />
                <select
                    value={selectedGame}
                    onChange={(e) => setSelectedGame(e.target.value)}
                    style={{
                        padding: '5px 10px',
                        borderRadius: '5px',
                        border: '1px solid #ccc',
                        width: '150px',
                    }}
                >
                    <option value="">All Games</option>
                    {/* Populate dropdown with unique game names */}
                    {[...new Set(allEvents.map((event) => event.boardGameName))].map((game) => (
                        <option key={game} value={game}>
                            {game}
                        </option>
                    ))}
                </select>
                <input
                    type="date"
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                    style={{
                        padding: '5px 10px',
                        borderRadius: '5px',
                        border: '1px solid #ccc',
                    }}
                />
                <Button
                    className="bg-black hover:bg-gray-800 text-white"
                    onClick={handleCreateEvent}
                >
                    Create Event
                </Button>
            </div>
            {/* Display loading or error messages */}
            {loading && <p>Loading events...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {/* Display event cards */}
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '15px', justifyContent: 'left' }}>
                {events.map((event, index) => (
                    <div
                        key={index}
                        className="relative bg-cover bg-center rounded-lg overflow-hidden cursor-pointer group"
                        style={{
                            width: '250px', // Fixed width
                            height: '250px', // Fixed height
                            backgroundImage: `url(${gameImages[boardGames.find(game => game.title === event.boardGameName)?.picture || 1]})`, // Match picture ID to image
                            backgroundSize: "cover",
                            backgroundPosition: "center",
                        }}
                        onClick={() => handleCardClick(event)}
                    >
                        {/* Title Overlay */}
                        <div className="absolute bottom-0 w-full bg-black bg-opacity-60 text-white text-center p-2 text-sm">
                            <div>{event.title}</div>
                            <div>{new Date(event.startDateTime).toLocaleDateString()}</div> {/* Display event date */}
                        </div>

                        {/* Translucent Effect */}
                        <div className="absolute inset-0 bg-black bg-opacity-20 opacity-0 group-hover:opacity-50 transition-opacity"></div>

                        {/* Centered More Info Button */}
                        <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                            <button
                                className="bg-white text-black px-4 py-2 rounded-lg shadow-md hover:bg-gray-200 font-bold"
                                onClick={() => handleCardClick(event)}
                            >
                                More Info
                            </button>
                        </div>
                    </div>
                ))}
            </div>
            {/* Show popup for the selected event */}
            {selectedEvent && (
                <EventPopup event={selectedEvent} onClose={handleClosePopup} />
            )}
            {/* Show popup for creating a new event */}
            {showCreatePopup && <EventCreationPopup onClose={handleCloseCreatePopup}/>}
        </div>
    );
};

export default Events;