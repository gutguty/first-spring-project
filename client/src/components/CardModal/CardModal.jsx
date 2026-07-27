import React from 'react';
import './CardModal.css'

const CardModal = ({card, onClose}) => {
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
                        onClick={() => {console.log("BUY LATER")}}
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