import React from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MainLayout from "@/layouts/MainLayout";
import AdminLayout from "@/layouts/AdminLayout";


const App = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<MainLayout />} />
                <Route path="/admin" element={<AdminLayout />} />
            </Routes>
        </BrowserRouter>
    );
};

export default App;