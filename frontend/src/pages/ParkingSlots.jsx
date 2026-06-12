import ResourcePage from './ResourcePage';

export default function ParkingSlots() {
  return <ResourcePage title="Parking Slots" endpoint="/parking-slots" initial={{ slotNumber: '', slotType: 'STANDARD', status: 'AVAILABLE', parkingLotId: '' }} fields={[
    { name: 'slotNumber', label: 'Slot Number' },
    { name: 'slotType', label: 'Slot Type', options: ['STANDARD', 'EV', 'ACCESSIBLE', 'VIP'] },
    { name: 'status', label: 'Status', options: ['AVAILABLE', 'RESERVED', 'OCCUPIED'] },
    { name: 'parkingLotId', label: 'Parking Lot ID', type: 'number' }
  ]} columns={[
    { key: 'id', label: 'ID' },
    { key: 'slotNumber', label: 'Slot' },
    { key: 'slotType', label: 'Type' },
    { key: 'status', label: 'Status' },
    { key: 'parkingLotName', label: 'Lot' }
  ]} />;
}
