import { Navigate, Route, Routes } from 'react-router-dom';
import AppLayout from './components/AppLayout';
import ProtectedRoute from './components/ProtectedRoute';
import AdminDashboard from './pages/AdminDashboard';
import Bookings from './pages/Bookings';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import ParkingLots from './pages/ParkingLots';
import ParkingSlots from './pages/ParkingSlots';
import Payments from './pages/Payments';
import Profile from './pages/Profile';
import Register from './pages/Register';
import Vehicles from './pages/Vehicles';
import WaitingQueue from './pages/WaitingQueue';

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route index element={<Dashboard />} />
          <Route path="vehicles" element={<Vehicles />} />
          <Route path="parking-lots" element={<ParkingLots />} />
          <Route path="parking-slots" element={<ParkingSlots />} />
          <Route path="bookings" element={<Bookings />} />
          <Route path="payments" element={<Payments />} />
          <Route path="waiting-queue" element={<WaitingQueue />} />
          <Route path="profile" element={<Profile />} />
          <Route element={<ProtectedRoute adminOnly />}>
            <Route path="admin" element={<AdminDashboard />} />
          </Route>
        </Route>
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
