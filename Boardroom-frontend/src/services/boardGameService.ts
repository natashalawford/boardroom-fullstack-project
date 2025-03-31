export interface BoardGame {
    title: string;
    description: string;
    playersNeeded: number;
    picture: number;
}

const API_BASE_URL = 'http://localhost:8080';

export const saveBoardGame = async (boardGame: BoardGame): Promise<BoardGame> => {
    const response = await fetch(`${API_BASE_URL}/boardgame`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(boardGame),
    });

    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.errors?.[0] || 'Somethingn went wrong');
    }

    return response.json();
};