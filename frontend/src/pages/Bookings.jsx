import ResourcePage from './ResourcePage';

export default function Bookings() {
  return <ResourcePage title="Bookings" endpoint="/bookings" initial={{ userId: '', vehicleId: '', slotId: '', bookingDate: new Date().toISOString().slice(0, 10), startTime: '08:00', endTime: '10:00', bookingStatus: 'CONFIRMED' }} fields={[
    { name: 'userId', label: 'User ID', type: 'number' },
    { name: 'vehicleId', label: 'Vehicle ID', type: 'number' },
    { name: 'slotId', label: 'Slot ID', type: 'number' },
    { name: 'bookingDate', label: 'Booking Date', type: 'date' },
    { name: 'startTime', label: 'Start Time', type: 'time' },
    { name: 'endTime', label: 'End Time', type: 'time' },
    { name: 'bookingStatus', label: 'Status', options: ['PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED'] }
  ]} columns={[
    { key: 'id', label: 'ID' },
    { key: 'userName', label: 'User' },
    { key: 'vehicleNumber', label: 'Vehicle' },
    { key: 'slotNumber', label: 'Slot' },
    { key: 'bookingDate', label: 'Date' },
    { key: 'bookingStatus', label: 'Status' }
  ]} />;
}
