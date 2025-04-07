import React, { useEffect, useState } from 'react';
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import monopoly from "../assets/monopoly.png";

import {
  deleteSpecificBoardGame,
  getSpecificBoardGamesByOwner,
  SpecificBoardGame,
  getBoardGameByTitle // <-- import here
} from "../services/AccountDetailsService";
import { useAuth } from "@/auth/UserAuth";

import image1 from '../assets/games/image1.jpg'
import image2 from '../assets/games/image2.jpg'
import image3 from '../assets/games/image3.jpg'
import image4 from '../assets/games/image4.jpg'
import image5 from '../assets/games/image5.jpg'

const gameImages: { [key: number]: string } = {
  1: image1,
  2: image2,
  3: image3,
  4: image4,
  5: image5
};

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
  const [ownedGames, setOwnedGames] = useState<SpecificBoardGame[]>([]);
  const [imageMap, setImageMap] = useState<{ [key: number]: number }>({});
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (userData?.id) {
      getSpecificBoardGamesByOwner(userData.id)
        .then(async (games) => {
          setOwnedGames(games);

          // Load picture values by board game title
          const imageMapData: { [key: number]: number } = {};
          for (const game of games) {
            try {
              const boardGame = await getBoardGameByTitle(game.boardGameTitle);
              imageMapData[game.id] = boardGame.picture; // Assuming picture is a number 1–5
            } catch (err) {
              imageMapData[game.id] = 0; // fallback
            }
          }
          setImageMap(imageMapData);
        })
        .catch((err) => setError(err.message));
    }
  }, [userData]);

  return (
    <div className="w-[60%] ml-10 mr-20">
      <h2 className="text-lg font-semibold mb-4">Owned Games</h2>

      <Card className="p-0 w-full max-w-4xl h-96 flex flex-col border rounded-lg p-4 shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200 bg-white outline outline-1 outline-gray-300">
        <ScrollArea className="w-full whitespace-nowrap">
          <div className="flex w-max gap-4 px-2">
            {ownedGames.map((game) => {
              const pictureId = imageMap[game.id];
              const image = gameImages[pictureId] || monopoly;

              return (
                <div
                  key={game.id}
                  className="relative w-64 h-64 bg-cover bg-center rounded-lg overflow-hidden cursor-pointer flex-shrink-0 group"
                  style={{ backgroundImage: `url(${image})` }}
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
                          setInfo({ id: game.id, status: game.status });
                        }}
                      >
                        Update
                      </Button>
                      <Button
                        onClick={async () => {
                          try {
                            await deleteSpecificBoardGame(game.id);
                            setOwnedGames(prev => prev.filter(g => g.id !== game.id));
                          } catch (err: any) {
                            setError(err.message || "Failed to delete game.");
                          }
                        }}
                        className="bg-white text-black px-4 py-2 rounded-lg shadow-md hover:bg-red-200"
                      >
                        Delete
                      </Button>
                    </div>
                  </div>
                </div>
              );
            })}
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