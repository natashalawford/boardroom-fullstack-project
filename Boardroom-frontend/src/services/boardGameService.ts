export interface BoardGame {
    title: string;
    description: string;
    playersNeeded: number;
    picture: number;
}

export interface SpecificBoardGame {
    boardGame: string;
    gameStatus: string;
    description: string;
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
        throw new Error(errorResponse.errors?.[0] || 'Something went wrong');
    }

    return response.json();
};

export const saveSpecificBoardGame = async (specificBoardGame: SpecificBoardGame): Promise<SpecificBoardGame> => {
    const response = await fetch(`${API_BASE_URL}/specificboardgame`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(specificBoardGame),
    });

    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.errors?.[0] || 'Something went wrong');
    }

    return response.json();
};

export const fetchBoardGames = async (): Promise<BoardGame[]> => {
    const response = await fetch(`${API_BASE_URL}/boardgame`);
    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(errorResponse.errors?.[0] || 'Something went wrong when fetching the board games');
    }
    return response.json();
}