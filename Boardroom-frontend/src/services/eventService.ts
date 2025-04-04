export interface Event {
    id: number;
    title: string;
    boardGameName: string;
    description: string;
    location: string;
    startDateTime: string;
    endDateTime: string;
    maxParticipants: number;
    hostId: number;
}

const API_BASE_URL = 'http://localhost:8080';

export const fetchEvents = async (): Promise<Event[]> => {
    const response = await fetch(`${API_BASE_URL}/events`);
    if (!response.ok) {
        throw new Error('Failed to fetch events');
    }
    return response.json();
};

export const fetchRegistrationsForEvent = async (eventId: number): Promise<any[]> => {
    const response = await fetch(`${API_BASE_URL}/registration/event/${eventId}`);
    if (!response.ok) {
        throw new Error('Failed to fetch registrations for the event');
    }
    return response.json();
};

export const fetchHostName = async (hostId: number): Promise<string> => {
    const response = await fetch(`${API_BASE_URL}/people/${hostId}`);
    if (!response.ok) {
        throw new Error('Failed to fetch host information');
    }
    const data = await response.json();
    return data.name;
};

export const createEvent = async (eventData: any): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/events`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(eventData),
    });
    if (!response.ok) {
        const errorData = await response.json();
        const errorMessage = errorData.errors?.[0] || 'Failed to create event';
        throw new Error(errorMessage);
    }
};

