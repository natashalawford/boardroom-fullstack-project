import React from 'react';
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import monopoly from "../assets/monopoly.png";

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

const exampleGames: OwnedGame[] = [
  {
    id: 1,
    description: "Classic property trading board game.",
    picture: 1,
    status: "AVAILABLE",
    boardGameTitle: "Monopoly",
    ownerId: 101,
  },
  {
    id: 2,
    description: "Fast-paced buying and selling fun.",
    picture: 1,
    status: "BORROWED",
    boardGameTitle: "Monopoly - City Edition",
    ownerId: 101,
  },
  {
    id: 3,
    description: "Build your empire and bankrupt your friends.",
    picture: 1,
    status: "AVAILABLE",
    boardGameTitle: "Monopoly - Retro Edition",
    ownerId: 101,
  },
  {
    id: 4,
    description: "Limited edition Monopoly with new rules.",
    picture: 1,
    status: "AVAILABLE",
    boardGameTitle: "Monopoly - Collector's Edition",
    ownerId: 101,
  },
];

interface OwnedGamesListProps {
  openModal: () => void;
  setInfo: (boardGameInfo: OwnedGameUpdate) => void;
}

const OwnedGamesList: React.FC<OwnedGamesListProps> = ({
  openModal,
  setInfo,
}: OwnedGamesListProps) => {
  return (
    <div className="w-full mt-6 ml-10 mr-10">
      <h2 className="text-lg font-semibold mb-4">Owned Games</h2>

      <Card className="rounded-2xl shadow-md border w-full max-w-[750px] p-4">
        <ScrollArea className="w-full whitespace-nowrap">
          <div className="flex w-max gap-4 px-2 pb-4">
            {exampleGames.map((game) => (
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
                    <Button className="bg-white text-black px-4 py-2 rounded-lg shadow-md hover:bg-red-200">
                      Delete
                    </Button>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <ScrollBar orientation="horizontal" />
        </ScrollArea>
      </Card>
    </div>
  );
};

export default OwnedGamesList;
