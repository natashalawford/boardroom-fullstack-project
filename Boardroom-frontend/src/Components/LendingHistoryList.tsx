import { ScrollArea } from "@/components/ui/scroll-area";
import { Card } from "@/components/ui/card";
import { getBorrowRequestsByOwnerAndStatus } from "@/services/AccountDetailsService";
import { useEffect, useState } from "react";
import { useAuth } from "@/auth/UserAuth";

type BorrowRequest = {
  id: number;
  status: string;
  requestStartDate: string;
  requestEndDate: string;
  personName: string;
  specificBoardGameTitle: string;
};

const LendingHistoryList = () => {
  const { userData } = useAuth(); 
  const [requests, setRequests] = useState<BorrowRequest[]>([]);

  useEffect(() => {
    let intervalId: NodeJS.Timeout;

    const fetchRequests = () => {
      if (userData?.id) {
        getBorrowRequestsByOwnerAndStatus(userData.id, "ACCEPTED")
          .then(setRequests)
          .catch(console.error);
      }
    };

    fetchRequests(); // Initial fetch

    // Set up interval to refresh every 10 seconds
    intervalId = setInterval(fetchRequests, 5000);

    return () => clearInterval(intervalId); // Cleanup on unmount
  }, [userData]);

  return (
    <>
      <h2 className="text-lg font-semibold mb-2">Lending History</h2>
      <Card className="p-0 w-full max-w-4xl h-96 flex flex-col border rounded-lg p-4 shadow-md transition duration-300 hover:shadow-lg hover:scale-103 border-gray-200 bg-white outline outline-1 outline-gray-300">
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
