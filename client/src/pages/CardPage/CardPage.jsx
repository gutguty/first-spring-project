import React, {useEffect, useState} from 'react';
import './CardPage.css'
import {useParams} from "react-router-dom";
import {getCard} from "@/api/api.js";

const CardPage = () => {
    const { id } = useParams()
    const [card, setCard] = useState(null)

    useEffect(() => {
        const fetchCard = async () => {
            try {
                const response = await getCard(id);
                setCard(response)
            } catch (err) {
                console.error(err.message)
            }
        }
        fetchCard()
    },[id])

    if (!card) return <div>Загрузка...</div>

    return (
        <div>
            <img src={card.image} alt={card.title} />
            <h1>{card.title}</h1>
            <p>{card.price}</p>
        </div>
    );
};

export default CardPage;