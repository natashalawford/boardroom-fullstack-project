import React from 'react'

interface ReviewProps {
    stars: number;
    comment: string;
    authorName: string;
    timestamp: string;
}

export const Review: React.FC<ReviewProps> = ({ stars, comment, authorName, timestamp }) => {
    return (
        <div className="p-5 rounded-lg mb-4 shadow-md">
            <div className="flex justify-between items-center">
                <span className="font-semibold">{`${authorName}`}</span>
                <span className="text-gray-500">{timestamp}</span>
            </div>
            <div className="flex items-center">
                {Array.from({ length: stars }, (_, index) => (
                    <span key={index} className="text-yellow-500">★</span>
                ))}
            </div>
            <p>{comment}</p>
        </div>
    )
}