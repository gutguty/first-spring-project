import React, {useEffect, useState} from 'react';
import './MainLayout.css'
import {getAll} from "@/api/api.js";
import Card from "@/components/Card/index.js";
import CardModal from "@/components/CardModal/index.js";

const MainLayout = () => {
    const [cards, setCards] = useState([])
    const [selectCard, setSelectCard] = useState(null)

    useEffect(() => {
        const fetchCards = async () => {
            try {
                const response = await getAll();
                setCards(response)
            } catch (err) {
                console.error(err.message)
            }
        }
        fetchCards()
    },[])

    return (
        <div className="main">
            <ul className="main__list">
                {cards.map((card) => (
                    <li className="main__list-item" key={card.id}>
                        <Card
                            title={card.title}
                            price={card.price}
                            image={card.image}
                            onCardClick={() => setSelectCard(card)}
                        />
                    </li>
                ))}
            </ul>

            {selectCard && (
                <CardModal
                    card={selectCard}
                    onClose={() => setSelectCard(null)}
                />
            )}
        </div>
    );
};

export default MainLayout;