import React, { useEffect, useState } from 'react';
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import monopoly from "../assets/monopoly.png";

import {
  deleteSpecificBoardGame,
  getSpecificBoardGamesByOwner,
  SpecificBoardGameResponseDto
} from "../services/AccountDetailsService";
import { useAuth } from "@/auth/UserAuth"; 

interface OwnedGame {
  id: number;
  description: string;
  picture: number;
  status: string;
  boardGameTitle: string;
  ownerId: number;
}

export interface OwnedGameUpdate {
  id: number;
  status: string;
}

interface OwnedGamesListProps {
  openModal: () => void;
  setInfo: (boardGameInfo: OwnedGameUpdate) => void;
}

const OwnedGamesList: React.FC<OwnedGamesListProps> = ({
  openModal,
  setInfo,
}: OwnedGamesListProps) => {
  const { userData } = useAuth();
  const [ownedGames, setOwnedGames] = useState<SpecificBoardGameResponseDto[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (userData?.id) {
      getSpecificBoardGamesByOwner(userData.id)
        .then(setOwnedGames)
        .catch((err) => setError(err.message));
    }
  }, [userData]);

  return (
    <div className="w-full mt-6 ml-10 mr-10">
      <h2 className="text-lg font-semibold mb-4">Owned Games</h2>

      <Card className="rounded-2xl shadow-md border w-full max-w-[750px] p-4">
        <ScrollArea className="w-full whitespace-nowrap">
          <div className="flex w-max gap-4 px-2 pb-4">
            {ownedGames.map((game) => (
              <div
                key={game.id}
                className="relative w-64 h-64 bg-cover bg-center rounded-lg overflow-hidden cursor-pointer flex-shrink-0 group"
                style={{
                  backgroundImage: `url(${monopoly})`,
                }}
              >
                <div className="absolute bottom-0 w-full bg-black bg-opacity-60 text-white text-center p-2 text-sm">
                  {game.boardGameTitle}
                </div>

                <div className="absolute inset-0 bg-black bg-opacity-20 opacity-0 group-hover:opacity-50 transition-opacity" />

                <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                  <div className="flex flex-col gap-2">
                    <Button
                      className="bg-white text-black px-4 py-2 rounded-lg shadow-md hover:bg-gray-200"
                      onClick={() => {
                        openModal();
                        console.log(game.description);
                        setInfo({
                          id: game.id,
                          status: game.status
                        });
                      }}
                    >
                      Update
                    </Button>
                    <Button onClick={async () => {
                      try {
                        await deleteSpecificBoardGame(game.id);
                        setOwnedGames(prev => prev.filter(g => g.id !== game.id));
                      } catch (err: any) {
                        setError(err.message || "Failed to delete game.");
                      }
                    }} className="bg-white text-black px-4 py-2 rounded-lg shadow-md hover:bg-red-200">
                      Delete
                    </Button>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <ScrollBar orientation="horizontal" />
        </ScrollArea>

        {error && (
          <p className="text-red-600 text-sm mt-2">
            Error loading games: {error}
          </p>
        )}
      </Card>
    </div>
  );
};

export default OwnedGamesList;
