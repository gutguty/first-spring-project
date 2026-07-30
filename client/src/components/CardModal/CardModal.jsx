import React from 'react';
import './CardModal.css'
import {useNavigate} from "react-router-dom";

const CardModal = ({card, onClose}) => {
    const navigate = useNavigate()

    return (
        <div className="cardModal">
            <div className="cardModal__overlay" onClick={() => onClose()}></div>
            <div className="cardModal__inner">
                <button className="cardModal__button-close" onClick={onClose}>X</button>
                <div className="cardModal__image">
                    <img src={`${card.image}`} alt="image"/>
                </div>
                <div className="cardModal__description">
                    <div className="cardModal__price">
                        <p>{card.price}</p>
                    </div>
                    <div className="cardModal__title">{card.title}</div>
                    <button
                        className="cardModal__button"
                        onClick={() => navigate(`/cards/${card.id}`)}
                        aria-label="Buy"
                    >
                        Купить
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CardModal;