import React, { useRef, useState } from 'react';

interface EventPopupProps {
    event: {
        id: number;
        title: string;
        boardGameName: string;
        description: string;
        location: string;
        startDateTime: string;
        endDateTime: string;
        maxParticipants: number;
        hostId: number;
    };
    personId: number; // Add personId as a prop
    onClose: () => void;
}

const EventPopup: React.FC<EventPopupProps> = ({ event, personId, onClose }) => {
    const popupRef = useRef<HTMLDivElement>(null);
    const [message, setMessage] = useState<string | null>(null); // For success/error messages

    const handleOverlayClick = (e: React.MouseEvent) => {
        if (popupRef.current && !popupRef.current.contains(e.target as Node)) {
            onClose();
        }
    };

    const handleRegister = async () => {
        try {
            const response = await fetch(`http://localhost:8080/registration/${personId}/${event.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to register for the event');
            }

            setMessage('Successfully registered for the event!');
        } catch (error: any) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
        <div
            onClick={handleOverlayClick}
            style={{
                position: 'fixed',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                backgroundColor: 'rgba(0, 0, 0, 0.5)',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                zIndex: 1000,
            }}
        >
            <div
                ref={popupRef}
                style={{
                    backgroundColor: '#303036',
                    padding: '20px',
                    borderRadius: '10px',
                    width: '400px',
                    boxShadow: '0 2px 10px rgba(0, 0, 0, 0.2)',
                }}
            >
                <h2>{event.title}</h2>
                <p><strong>Game:</strong> {event.boardGameName}</p>
                <p><strong>Description:</strong> {event.description}</p>
                <p><strong>Location:</strong> {event.location}</p>
                <p><strong>Start:</strong> {new Date(event.startDateTime).toLocaleString()}</p>
                <p><strong>End:</strong> {new Date(event.endDateTime).toLocaleString()}</p>
                <p><strong>Max Participants:</strong> {event.maxParticipants}</p>
                <p><strong>Host ID:</strong> {event.hostId}</p>
                <div style={{ display: 'flex', marginTop: '10px', justifyContent: 'right' }}>
                    <button
                        onClick={handleRegister}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#30BCED',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                        }}
                    >
                        Register for Event
                    </button>
                </div>
                {message && (
                    <div
                        style={{
                            marginTop: '20px',
                            padding: '10px',
                            backgroundColor: message.startsWith('Error') ? 'red' : 'green',
                            color: '#fff',
                            borderRadius: '5px',
                        }}
                    >
                        {message}
                    </div>
                )}
            </div>
        </div>
    );
};

export default EventPopup;