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
                    <tr className="admin__table-row">
                        <th className="admin__table-head">ID</th>
                        <th className="admin__table-head">Title</th>
                        <th className="admin__table-head">Price</th>
                        <th className="admin__table-head">Image</th>
                        <th className="admin__table-head">Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {cards.map((card) => (
                        <tr className="admin__table-row" key={card.id}>
                            <td className="admin__table-cell">{card.id}</td>
                            <td className="admin__table-cell">{card.title}</td>
                            <td className="admin__table-cell">{card.price}</td>
                            <td className="admin__table-cell">{card.image}</td>
                            <td className="admin__table-cell admin__table-cell--actions">
                                <button
                                    className="admin__table-button"
                                    onClick={() => setEditCard(card)}
                                >
                                    Обновить
                                </button>
                                <button
                                    className="admin__table-button"
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