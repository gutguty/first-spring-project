import React, {useEffect, useState} from 'react';
import './AdminLayout.css'
import {createCard, deleteCard, getAll, updateCard} from "@/api/api.js";
import Modal from "@/components/Modal/index.js";

const AdminLayout = () => {
    const [cards, setCards] = useState([])
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [editCard, setEditCard] = useState(null)

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

    const handleDeleteCard = async (id) => {
        if (!confirm("Удалить карточку?")) return
        await deleteCard(id)
        setCards(cards.filter(card => card.id !== id))
    }

    const handleCreateCard = async (card) => {
        const created = await createCard(card)
        setCards([...cards, created])
    }

    const handleUpdateCard = async (newCard) => {
        const updated = await updateCard(editCard.id, newCard)
        setCards(cards.map(card => card.id === editCard.id ? updated : card))
        setEditCard(null)
    }

    return (
        <div className="admin">
            <h1 className="admin__title">Админ-панель</h1>
            <section className="admin__section">
                <button
                    className="admin__button"
                    onClick={() => setIsModalOpen(true)}
                >
                    Добавить
                </button>
                {(isModalOpen || editCard) && (
                    <Modal
                        onClose={() => {
                            setIsModalOpen(false)
                            setEditCard(null)
                        }}
                        onSave={editCard ? handleUpdateCard : handleCreateCard}
                        card={editCard}
                    />
                )}
                <table className="admin__table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Price</th>
                        <th>Image</th>
                    </tr>
                    </thead>
                    <tbody>
                    {cards.map((card) => (
                        <tr key={card.id}>
                            <td>{card.id}</td>
                            <td>{card.title}</td>
                            <td>{card.price}</td>
                            <td>{card.image}</td>
                            <td>
                                <button
                                    onClick={() => setEditCard(card)}
                                >
                                    Обновить
                                </button>
                                <button
                                    onClick={() => handleDeleteCard(card.id)}
                                >
                                    Удалить
                                </button>
                            </td>
                        </tr>
                        ))}
                    </tbody>
                </table>
            </section>
        </div>
    );
};

export default AdminLayout;