import React, { useState, useEffect } from 'react';
import EventPopup from './eventPopup';
import { fetchEvents, Event } from '../../services/eventService'; // Import the fetchEvents function
import monopoly from '../../assets/monopoly.png';

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

    // Fetch events from the backend when the component mounts
    useEffect(() => {
        const loadEvents = async () => {
            setLoading(true);
            setError('');
            try {
                const data = await fetchEvents();
                setAllEvents(data); // Store all events
                setEvents(data); // Initially display all events
            } catch (err: any) {
                setError(err.message || 'An error occurred');
            } finally {
                setLoading(false);
            }
        };

        loadEvents();
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
        console.log('Redirect to Create Event popup');
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
                <button
                    onClick={handleCreateEvent}
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#30BCED',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                    }}
                >
                    Create Event
                </button>
            </div>
            {/* Display loading or error messages */}
            {loading && <p>Loading events...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {/* Display event cards */}
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '15px', justifyContent: 'left' }}>
                {events.map((event) => (
                    <div
                        key={event.id}
                        onClick={() => handleCardClick(event)}
                        style={{
                            border: '1px solid #ccc',
                            borderRadius: '10px',
                            padding: '10px',
                            backgroundColor: '#f9f9f9',
                            boxShadow: '0 2px 5px rgba(0, 0, 0, 0.1)',
                            width: '200px',
                            textAlign: 'center',
                            cursor: 'pointer',
                        }}
                    >
                        {/* Add image for Monopoly */}
                        {event.boardGameName.toLowerCase() === 'monopoly' && (
                            <img
                                src={monopoly}
                                alt="Monopoly"
                                style={{
                                    width: '100%',
                                    height: '120px',
                                    objectFit: 'cover',
                                    borderRadius: '5px',
                                    marginBottom: '5px',
                                }}
                            />
                        )}
                        <h3 style={{ fontSize: '1.2em', margin: '5px 0', color: 'black' }}>{event.title}</h3>
                        <p style={{ fontSize: '0.9em', margin: '5px 0', color: 'black' }}><strong>Game:</strong> {event.boardGameName}</p>
                        <p style={{ fontSize: '0.8em', margin: '5px 0', color: 'black' }}><strong>Date:</strong> {new Date(event.startDateTime).toLocaleDateString()}</p>
                    </div>
                ))}
            </div>
            {/* Show popup for the selected event */}
            {selectedEvent && (
                <EventPopup event={selectedEvent} onClose={handleClosePopup} />
            )}
        </div>
    );
};

export default Events;