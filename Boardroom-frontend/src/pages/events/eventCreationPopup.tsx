import React, { useState } from 'react';

// Define color scheme constants
const TEXT_COLOR = '#fff';
const BACKGROUND_COLOR = '#303036';
const CLOSE_BUTTON_COLOR = '#ccc';
const CLOSE_BUTTON_TEXT_COLOR = '#000';
const CREATE_BUTTON_COLOR = '#30BCED';
const CREATE_BUTTON_TEXT_COLOR = '#fff';
const INPUT_BORDER_COLOR = '#ccc';

interface EventCreationPopupProps {
    onClose: () => void;
}

const EventCreationPopup: React.FC<EventCreationPopupProps> = ({ onClose }) => {
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        boardGameName: '',
        startDateTime: '',
        endDateTime: '',
        maxParticipants: '',
        location: '',
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log('Event Data:', formData);
        // Add logic to send formData to the backend
    };

    return (
        <div
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
                style={{
                    backgroundColor: BACKGROUND_COLOR,
                    padding: '20px',
                    borderRadius: '10px',
                    width: '400px',
                    boxShadow: '0 2px 10px rgba(0, 0, 0, 0.2)',
                }}
            >
                <h2 style={{ color: TEXT_COLOR, marginBottom: '20px' }}>Create New Event</h2>
                <form onSubmit={handleSubmit}>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Title:</label>
                        <input
                            type="text"
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Description:</label>
                        <textarea
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Board Game Name:</label>
                        <input
                            type="text"
                            name="boardGameName"
                            value={formData.boardGameName}
                            onChange={handleChange}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Start Date & Time:</label>
                        <input
                            type="datetime-local"
                            name="startDateTime"
                            value={formData.startDateTime}
                            onChange={handleChange}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>End Date & Time:</label>
                        <input
                            type="datetime-local"
                            name="endDateTime"
                            value={formData.endDateTime}
                            onChange={handleChange}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Max Participants:</label>
                        <input
                            type="number"
                            name="maxParticipants"
                            value={formData.maxParticipants}
                            onChange={handleChange}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Location:</label>
                        <input
                            type="text"
                            name="location"
                            value={formData.location}
                            onChange={handleChange}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px' }}>
                        <button
                            type="submit"
                            style={{
                                padding: '10px 20px',
                                backgroundColor: CREATE_BUTTON_COLOR,
                                color: CREATE_BUTTON_TEXT_COLOR,
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                            }}
                        >
                            Create
                        </button>
                        <button
                            onClick={onClose}
                            style={{
                                padding: '10px 20px',
                                backgroundColor: CLOSE_BUTTON_COLOR,
                                color: CLOSE_BUTTON_TEXT_COLOR,
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                            }}
                        >
                            Close
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default EventCreationPopup;
