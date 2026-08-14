import React, {useState} from 'react';
import './Card.css'

const Card = ({title, price, image, onCardClick}) => {

    return (
        <div className="card">
            <div className="card__image">
                <img src={`${image}`} alt="image"/>
            </div>
            <div className="card__description">
                <div className="card__price">{price}</div>
                <div className="card__title">{title}</div>
                <button
                    className="card__button"
                    onClick={() => onCardClick()}
                >
                    Показать
                </button>
            </div>
        </div>
    );
};

export default Card;