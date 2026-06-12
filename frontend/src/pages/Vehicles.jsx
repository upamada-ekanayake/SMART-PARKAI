import ResourcePage from './ResourcePage';

export default function Vehicles() {
  return <ResourcePage title="Vehicles" endpoint="/vehicles" initial={{ userId: '', vehicleNumber: '', vehicleType: 'CAR', brand: '', model: '', color: '' }} fields={[
    { name: 'userId', label: 'User ID', type: 'number' },
    { name: 'vehicleNumber', label: 'Vehicle Number' },
    { name: 'vehicleType', label: 'Vehicle Type', options: ['CAR', 'MOTORBIKE', 'VAN', 'TRUCK', 'EV'] },
    { name: 'brand', label: 'Brand' },
    { name: 'model', label: 'Model' },
    { name: 'color', label: 'Color' }
  ]} columns={[
    { key: 'id', label: 'ID' },
    { key: 'ownerName', label: 'Owner' },
    { key: 'vehicleNumber', label: 'Number' },
    { key: 'vehicleType', label: 'Type' },
    { key: 'brand', label: 'Brand' },
    { key: 'model', label: 'Model' }
  ]} />;
}
