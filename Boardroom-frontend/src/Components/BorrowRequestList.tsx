import { useEffect, useState } from "react";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { getBorrowRequestsByPersonAndStatus, updateBorrowRequestStatus } from "../services/AccountDetailsService";
import { useAuth } from "@/auth/UserAuth";

type BorrowRequest = {
  id: number;
  status: string;
  requestStartDate: string;
  requestEndDate: string;
  personName: string;
  specificBoardGameTitle: string;
};

const BorrowRequestList = () => {
  const { userData } = useAuth();
  const [requests, setRequests] = useState<BorrowRequest[]>([]);

  useEffect(() => {
    if (userData?.id) {
      getBorrowRequestsByPersonAndStatus(userData.id, "PENDING")
        .then(setRequests)
        .catch(console.error);
    }
  }, [userData]);

  const handleStatusUpdate = (id: number, status: string) => {
    if (!userData?.id) return;

    updateBorrowRequestStatus(id, status)
      .then(() => getBorrowRequestsByPersonAndStatus(userData.id, "PENDING"))
      .then(setRequests)
      .catch(console.error);
  };

  return (
    <>
      <h2 className="text-lg font-semibold mb-2">Borrow Requests</h2>
      <Card className="p-0 w-full max-w-4xl h-96 flex flex-col">
        <ScrollArea className="h-full">
          <table className="w-full text-sm text-left border-separate border-spacing-y-2">
            <thead className="text-xs uppercase text-muted-foreground">
              <tr>
                <th className="px-8 py-2">Borrower</th>
                <th className="px-8 py-2">Game</th>
                <th className="px-8 py-2">Borrow Date</th>
                <th className="px-8 py-2">Return Date</th>
                <th className="px-8 py-2 text-center"></th>
              </tr>
            </thead>
            <tbody>
              {requests.map((req) => (
                <tr key={req.id} className="bg-muted rounded-md">
                  <td className="px-8 py-2 border-t border-gray-200">{req.personName}</td>
                  <td className="px-8 py-2 border-t border-gray-200">{req.specificBoardGameTitle}</td>
                  <td className="px-8 py-2 border-t border-gray-200">
                    {new Date(req.requestStartDate).toISOString().split("T")[0]}
                  </td>
                  <td className="px-8 py-2 border-t border-gray-200">
                    {new Date(req.requestEndDate).toISOString().split("T")[0]}
                  </td>
                  <td className="px-8 py-2 border-t border-gray-200 flex justify-center gap-2">
                    <Button
                      variant="outline"
                      className="hover:bg-gray-700 hover:text-white w-30 self-end"
                      onClick={() => handleStatusUpdate(req.id, "ACCEPTED")}
                    >
                      Accept
                    </Button>
                    <Button
                      variant="outline"
                      className="hover:bg-gray-700 hover:text-white w-30 self-end"
                      onClick={() => handleStatusUpdate(req.id, "DECLINED")}
                    >
                      Decline
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </ScrollArea>
      </Card>
    </>
  );
};

export default BorrowRequestList;
