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

