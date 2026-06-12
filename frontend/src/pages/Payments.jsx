import ResourcePage from './ResourcePage';

export default function Payments() {
  return <ResourcePage title="Payments" endpoint="/payments" initial={{ bookingId: '', amount: '', paymentMethod: 'CARD', paymentStatus: 'PAID' }} fields={[
    { name: 'bookingId', label: 'Booking ID', type: 'number' },
    { name: 'amount', label: 'Amount', type: 'number' },
    { name: 'paymentMethod', label: 'Method', options: ['CASH', 'CARD', 'ONLINE'] },
    { name: 'paymentStatus', label: 'Status', options: ['PENDING', 'PAID', 'FAILED', 'REFUNDED'] }
  ]} columns={[
    { key: 'id', label: 'ID' },
    { key: 'bookingId', label: 'Booking' },
    { key: 'amount', label: 'Amount' },
    { key: 'paymentMethod', label: 'Method' },
    { key: 'paymentStatus', label: 'Status' },
    { key: 'paymentDate', label: 'Date' }
  ]} />;
}
