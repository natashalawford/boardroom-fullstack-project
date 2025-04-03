import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useAuth } from '../../auth/UserAuth';

// Define color scheme constants
const TEXT_COLOR = '#000';
const BACKGROUND_COLOR = '#fff';
const CLOSE_BUTTON_COLOR = '#ccc';
const CLOSE_BUTTON_TEXT_COLOR = '#000';
const CREATE_BUTTON_COLOR = '#30BCED';
const CREATE_BUTTON_TEXT_COLOR = '#fff';
const INPUT_BORDER_COLOR = '#808080';

// Validation schema using zod
const formSchema = z.object({
    title: z.string().min(1, { message: 'Title is required.' }),
    description: z.string().min(1, { message: 'Description is required.' }),
    boardGameName: z.string().min(1, { message: 'Board game name is required.' }),
    startDateTime: z.string().min(1, { message: 'Start date and time are required.' }),
    endDateTime: z.string().min(1, { message: 'End date and time are required.' }),
    maxParticipants: z
        .preprocess((value) => (typeof value === 'string' ? parseInt(value, 10) : value), z.number().min(1, { message: 'Max participants must be at least 1.' })),
    location: z.string().min(1, { message: 'Location is required.' }),
});

type FormData = z.infer<typeof formSchema>;

interface EventCreationPopupProps {
    onClose: () => void;
}

const EventCreationPopup: React.FC<EventCreationPopupProps> = ({ onClose }) => {
    const { userData } = useAuth();
    const [boardGames, setBoardGames] = useState<string[]>([]); // State to store board games
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<FormData>({
        resolver: zodResolver(formSchema),
    });

    const [message, setMessage] = React.useState<string | null>(null); // For success/error messages

    // Fetch board games from the backend
    useEffect(() => {
        const fetchBoardGames = async () => {
            try {
                const response = await fetch('http://localhost:8080/boardgame', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch board games');
                }

                const data = await response.json();
                const boardGameTitles = data.map((game: { title: string }) => game.title); // Extract titles
                setBoardGames(boardGameTitles);
            } catch (error) {
                console.error('Error fetching board games:', error);
            }
        };

        fetchBoardGames();
    }, []);

    const onSubmit = async (data: FormData) => {
        setMessage(null); // Clear previous messages
        const eventData = {
            ...data,
            hostId: userData?.id, // Ensure userData has an 'id' property
        };
        try {
            const response = await fetch('http://localhost:8080/events', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(eventData),
            });

            if (!response.ok) {
                const errorData = await response.json();
                const errorMessage = errorData.errors?.[0] || 'Failed to create event';
                console.error('Error response:', errorData);
                throw new Error(errorMessage || 'Failed to create event');
            }

            setMessage('Event created successfully!');
            onClose(); // Close the popup after successful creation
        } catch (error: any) {
            setMessage(`Error: ${error.message}`);
        }
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
                <h2 style={{ color: TEXT_COLOR, marginBottom: '20px', fontWeight: 'bold' }}>Create New Event</h2>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Title:</label>
                        <input
                            type="text"
                            {...register('title')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                        {errors.title && <p style={{ color: 'red' }}>{errors.title.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Description:</label>
                        <textarea
                            {...register('description')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                        {errors.description && <p style={{ color: 'red' }}>{errors.description.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Board Game Name:</label>
                        <select
                             {...register('boardGameName')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                            >
                            <option value="">Select a board game</option>
                            {boardGames.map((game) => (
                                <option key={game} value={game}>
                                    {game}
                                </option>
                            ))}
                        </select>
                        {errors.boardGameName && <p style={{ color: 'red' }}>{errors.boardGameName.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Start Date & Time:</label>
                        <input
                            type="datetime-local"
                            {...register('startDateTime')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                                colorScheme: 'light',
                            }}
                        />
                        {errors.startDateTime && <p style={{ color: 'red' }}>{errors.startDateTime.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>End Date & Time:</label>
                        <input
                            type="datetime-local"
                            {...register('endDateTime')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                                colorScheme: 'light',
                            }}
                        />
                        {errors.endDateTime && <p style={{ color: 'red' }}>{errors.endDateTime.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Max Participants:</label>
                        <input
                            type="number"
                            {...register('maxParticipants')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                        {errors.maxParticipants && <p style={{ color: 'red' }}>{errors.maxParticipants.message}</p>}
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <label style={{ color: TEXT_COLOR }}>Location:</label>
                        <input
                            type="text"
                            {...register('location')}
                            style={{
                                width: '100%',
                                padding: '5px',
                                borderRadius: '5px',
                                border: `1px solid ${INPUT_BORDER_COLOR}`,
                                marginTop: '5px',
                            }}
                        />
                        {errors.location && <p style={{ color: 'red' }}>{errors.location.message}</p>}
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

export default EventCreationPopup;