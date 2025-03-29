import React, { useRef } from 'react';

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
    onClose: () => void;
}

const EventPopup: React.FC<EventPopupProps> = ({ event, onClose }) => {
    const popupRef = useRef<HTMLDivElement>(null);

    const handleOverlayClick = (e: React.MouseEvent) => {
        if (popupRef.current && !popupRef.current.contains(e.target as Node)) {
            onClose();
        }
    };

    const handleRegister = () => {
        console.log(`Registering for event with ID: ${event.id}`);
        // Add registration logic here
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
                <div style={{ display: 'flex',  marginTop: '10px', justifyContent: 'right' }}>
                    <button onClick={handleRegister}
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
            </div>
        </div>
    );
};

export default EventPopup;
