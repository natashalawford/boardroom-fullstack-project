import { ScrollArea } from "@/components/ui/scroll-area";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { getBorrowRequestsByPersonAndStatus } from "@/services/AccountDetailsService";
import { useEffect, useState } from "react";
import { useAuth } from "@/auth/UserAuth"; // ✅ import the auth context

type BorrowRequest = {
  id: number;
  status: string;
  requestStartDate: string;
  requestEndDate: string;
  personName: string;
  specificBoardGameTitle: string;
};

const LendingHistoryList = () => {
  const { userData } = useAuth(); // ✅ get the logged-in user
  const [requests, setRequests] = useState<BorrowRequest[]>([]);

  useEffect(() => {
    if (userData?.id) {
      getBorrowRequestsByPersonAndStatus(userData.id, "RETURNED")
        .then(setRequests)
        .catch(console.error);
    }
  }, [userData]);

  return (
    <>
      <h2 className="text-lg font-semibold mb-2">Lending History</h2>
      <Card className="p-0 w-full max-w-4xl h-96 flex flex-col">
        <ScrollArea className="h-full w-full">
          <table className="w-full text-sm text-left border-separate border-spacing-y-2">
            <thead className="text-xs uppercase text-muted-foreground">
              <tr>
                <th className="px-8 py-2">Borrower</th>
                <th className="px-8 py-2">Game</th>
                <th className="px-8 py-2">Borrow Date</th>
                <th className="px-8 py-2">Return Date</th>
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
                </tr>
              ))}
            </tbody>
          </table>
        </ScrollArea>
      </Card>
    </>
  );
};

export default LendingHistoryList;
