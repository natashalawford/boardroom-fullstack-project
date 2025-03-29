import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button'

const Games: React.FC = () => {
    const navigate = useNavigate();

    return (
        <div className='font-roboto p-8'>
            <h1 className='font-semibold'>Games Page</h1>
            <p className='pt-3'>Welcome to the Games Page! Here you can explore and play various games.</p>
        
            <div className='flex justify-start pt-3'>
                <Button variant='default' onClick={() => navigate('/games/new')} className='mr-2'>
                    Add New Game
                </Button>
            </div>
        </div>
    );
};

export default Games;