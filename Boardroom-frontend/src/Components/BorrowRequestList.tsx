import { ScrollArea } from "@/components/ui/scroll-area";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

const mockRequests = [
  {
    id: 1,
    borrower: "Alice",
    game: "Catan",
    borrowDate: "2025-03-01",
    returnDate: "2025-03-10",
  },
  {
    id: 2,
    borrower: "Bob",
    game: "Carcassonne",
    borrowDate: "2025-03-05",
    returnDate: "2025-03-12",
  },
  {
    id: 3,
    borrower: "Charlie",
    game: "Ticket to Ride",
    borrowDate: "2025-03-08",
    returnDate: "2025-03-15",
  },
  {
    id: 1,
    borrower: "Alice",
    game: "Catan",
    borrowDate: "2025-03-01",
    returnDate: "2025-03-10",
  },
  {
    id: 2,
    borrower: "Bob",
    game: "Carcassonne",
    borrowDate: "2025-03-05",
    returnDate: "2025-03-12",
  },
  {
    id: 3,
    borrower: "Charlie",
    game: "Ticket to Ride",
    borrowDate: "2025-03-08",
    returnDate: "2025-03-15",
  },
  {
    id: 1,
    borrower: "Alice",
    game: "Catan",
    borrowDate: "2025-03-01",
    returnDate: "2025-03-10",
  },
  {
    id: 2,
    borrower: "Bob",
    game: "Carcassonne",
    borrowDate: "2025-03-05",
    returnDate: "2025-03-12",
  },
  {
    id: 3,
    borrower: "Charlie",
    game: "Ticket to Ride",
    borrowDate: "2025-03-08",
    returnDate: "2025-03-15",
  },
];

const BorrowRequestList = () => {
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
              {mockRequests.map((req) => (
                <tr key={req.id} className="bg-muted rounded-md">
                  <td className="px-8 py-2">{req.borrower}</td>
                  <td className="px-8 py-2">{req.game}</td>
                  <td className="px-8 py-2">{req.borrowDate}</td>
                  <td className="px-8 py-2">{req.returnDate}</td>
                  <td className="px-8 py-2 flex justify-center gap-2">
                  <Button
                    variant="outline"
                    className="hover:bg-gray-700 hover:text-white w-30 self-end"
                    >
                    Accept
                  </Button>
                  <Button
                    variant="outline"
                    className="hover:bg-gray-700 hover:text-white w-30 self-end"
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
