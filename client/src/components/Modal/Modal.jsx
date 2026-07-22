import React, {useState} from 'react';
import './Modal.css'

const Modal = ({ onClose, onSave, card }) => {
    const [title, setTitle] = useState(card?.title || '')
    const [price, setPrice] = useState(card?.price || '')
    const [image, setImage] = useState(card?.image || '')

    const handleSave = () => {
        onSave({ title, price: Number(price), image })
        onClose()
    }

    return (
        <div className="modal">
            <div className="modal__overlay" onClick={onClose}></div>
            <div className="modal__inner">
                <button className="modal__button-close" onClick={onClose}>X</button>
                <h2 className="modal__header">Создать карточку</h2>

                <label className="modal__label">
                    Название
                    <input
                        type="text"
                        className="modal__input"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Название товара"
                    />
                </label>

                <label className="modal__label">
                    Цена
                    <input
                        type="number"
                        className="modal__input"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        placeholder="Цена"
                    />
                </label>

                <label className="modal__label">
                    Картинка
                    <input
                        type="text"
                        className="modal__input"
                        value={image}
                        onChange={(e) => setImage(e.target.value)}
                        placeholder="URL картинки"
                    />
                </label>

                <button className="modal__button-save" onClick={handleSave}>
                    Сохранить
                </button>
            </div>
        </div>
    );
};

export default Modal;