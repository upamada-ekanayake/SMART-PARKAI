import ResourcePage from './ResourcePage';

export default function ParkingLots() {
  return <ResourcePage title="Parking Lots" endpoint="/parking-lots" initial={{ name: '', address: '', description: '', totalSlots: 0, availableSlots: 0 }} fields={[
    { name: 'name', label: 'Name' },
    { name: 'address', label: 'Address' },
    { name: 'description', label: 'Description' },
    { name: 'totalSlots', label: 'Total Slots', type: 'number' },
    { name: 'availableSlots', label: 'Available Slots', type: 'number' }
  ]} columns={[
    { key: 'id', label: 'ID' },
    { key: 'name', label: 'Name' },
    { key: 'address', label: 'Address' },
    { key: 'totalSlots', label: 'Total' },
    { key: 'availableSlots', label: 'Available' }
  ]} />;
}
