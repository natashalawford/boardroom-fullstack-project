import React, { useState, useEffect } from 'react'
import { Button } from './ui/button'
import { RotateCcw } from 'lucide-react'

interface FileUploadProps {
  id: string
  value?: File | null // <--- Add a 'value' prop from parent
  onChange?: (file: File | null) => void
}

const FileUpload: React.FC<FileUploadProps> = ({ id, value, onChange }) => {
  const [uploadedFile, setUploadedFile] = useState<File | null>(value || null)

  useEffect(() => {
    // Sync local state whenever the parent changes the 'value' prop
    setUploadedFile(value || null)
  }, [value])

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0] || null
    setUploadedFile(file)
    onChange?.(file) // Ensure the parent sees the new file
  }

  const handleReplace = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0] || null
    setUploadedFile(file)
    onChange?.(file) // Ensure the parent sees the new file
  }

  return (
    <div className='flex items-center justify-center w-full'>
      {!uploadedFile ? (
        <label
          htmlFor={id}
          className='flex flex-col items-center justify-center w-full h-64 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 dark:hover:bg-gray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600'
        >
          <div className='flex flex-col items-center justify-center pt-5 pb-6'>
            <svg
              className='w-8 h-8 mb-4 text-gray-500 dark:text-gray-400'
              aria-hidden='true'
              xmlns='http://www.w3.org/2000/svg'
              fill='none'
              viewBox='0 0 20 16'
            >
              <path
                stroke='currentColor'
                strokeLinecap='round'
                strokeLinejoin='round'
                strokeWidth='2'
                d='M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2'
              />
            </svg>
            <p className='mb-2 text-sm text-gray-500 dark:text-gray-400'>
              <span className='font-semibold'>Click to upload</span>
            </p>
            <p className='text-xs text-gray-500 dark:text-gray-400'>
              PNG, JPEG, or JPG
            </p>
          </div>
          <input
            id={id}
            type='file'
            accept='.png, .jpg, .jpeg'
            className='hidden'
            onChange={handleFileChange}
          />
        </label>
      ) : (
        <div className='flex flex-col items-center justify-center w-full p-4 border border-gray-300 rounded-lg dark:border-gray-600 bg-gray-100 dark:bg-gray-700'>
          <p className='text-sm text-gray-700 dark:text-gray-200 mb-2'>
            Image: {uploadedFile.name}
          </p>
          <input
            id={`${id}-replace`}
            type='file'
            accept='.png, .jpg, .jpeg'
            className='hidden'
            onChange={handleReplace}
          />
          <Button
            className='mb-3 flex items-center space-x-1'
            onClick={() => document.getElementById(`${id}-replace`)?.click()}
          >
            <RotateCcw strokeWidth={3} />
            <span>Replace</span>
          </Button>
        </div>
      )}
    </div>
  )
}

export default FileUpload
