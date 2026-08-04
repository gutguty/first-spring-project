import React from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MainLayout from "@/layouts/MainLayout";
import AdminLayout from "@/layouts/AdminLayout";
import CardPage from "@/pages/CardPage/index.js";


const App = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<MainLayout />} />
                <Route path="/admin" element={<AdminLayout />} />
                <Route path="/cards/:id" element={<CardPage />} />
            </Routes>
        </BrowserRouter>
    );
};

export default App;