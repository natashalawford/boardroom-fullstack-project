import React from 'react'

interface ReviewProps {
  stars: number
  comment: string
  authorName: string
  timestamp: string
}

export const Review: React.FC<ReviewProps> = ({
  stars,
  comment,
  authorName,
  timestamp
}) => {
  return (
    <div className='p-1.5'>
      <div className='p-5 rounded-lg shadow-lg transition duration-300 hover:shadow-[0_0_10px_rgba(191, 191, 191,0.8)] hover:scale-103'>
        <div className='flex justify-between items-center'>
          <span className='font-semibold'>{`${authorName}`}</span>
          <span className='text-gray-500'>{timestamp}</span>
        </div>
        <div className='flex items-center'>
          {Array.from({ length: stars }, (_, index) => (
            <span key={index} className='text-yellow-500'>
              ★
            </span>
          ))}
        </div>
        <p>{comment}</p>
      </div>
    </div>
  )
}
