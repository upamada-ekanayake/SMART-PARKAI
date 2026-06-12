import { AppBar, Box, Divider, Drawer, IconButton, List, ListItemButton, ListItemIcon, ListItemText, Toolbar, Typography, useMediaQuery } from '@mui/material';
import Assessment from '@mui/icons-material/Assessment';
import BookOnline from '@mui/icons-material/BookOnline';
import CreditCard from '@mui/icons-material/CreditCard';
import Dashboard from '@mui/icons-material/Dashboard';
import DirectionsCar from '@mui/icons-material/DirectionsCar';
import Garage from '@mui/icons-material/Garage';
import Logout from '@mui/icons-material/Logout';
import Menu from '@mui/icons-material/Menu';
import Person from '@mui/icons-material/Person';
import Queue from '@mui/icons-material/Queue';
import SpaceDashboard from '@mui/icons-material/SpaceDashboard';
import WbSunny from '@mui/icons-material/WbSunny';
import DarkMode from '@mui/icons-material/DarkMode';
import { useState } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useColorMode } from '../theme/ColorModeProvider';

const navItems = [
  ['Dashboard', '/', Dashboard],
  ['Vehicles', '/vehicles', DirectionsCar],
  ['Parking Lots', '/parking-lots', Garage],
  ['Parking Slots', '/parking-slots', SpaceDashboard],
  ['Bookings', '/bookings', BookOnline],
  ['Payments', '/payments', CreditCard],
  ['Waiting Queue', '/waiting-queue', Queue],
  ['Profile', '/profile', Person],
  ['Admin Dashboard', '/admin', Assessment, true]
];

export default function AppLayout() {
  const [open, setOpen] = useState(false);
  const desktop = useMediaQuery('(min-width:900px)');
  const { logout, isAdmin, user } = useAuth();
  const { mode, toggleMode } = useColorMode();
  const navigate = useNavigate();
  const width = 260;

  const drawer = (
    <Box sx={{ width, height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Box sx={{ p: 2.5 }}>
        <Typography variant="h6">ParkSmart AI</Typography>
        <Typography variant="body2" color="text.secondary">{user?.firstName} {user?.lastName}</Typography>
      </Box>
      <Divider />
      <List sx={{ flex: 1 }}>
        {navItems.filter((item) => !item[3] || isAdmin).map(([label, path, Icon]) => (
          <ListItemButton key={path} component={NavLink} to={path} onClick={() => setOpen(false)} sx={{ mx: 1, borderRadius: 1 }}>
            <ListItemIcon><Icon /></ListItemIcon>
            <ListItemText primary={label} />
          </ListItemButton>
        ))}
      </List>
      <Divider />
      <List>
        <ListItemButton onClick={toggleMode} sx={{ mx: 1, borderRadius: 1 }}>
          <ListItemIcon>{mode === 'light' ? <DarkMode /> : <WbSunny />}</ListItemIcon>
          <ListItemText primary={mode === 'light' ? 'Dark mode' : 'Light mode'} />
        </ListItemButton>
        <ListItemButton onClick={() => { logout(); navigate('/login'); }} sx={{ mx: 1, borderRadius: 1 }}>
          <ListItemIcon><Logout /></ListItemIcon>
          <ListItemText primary="Logout" />
        </ListItemButton>
      </List>
    </Box>
  );

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh' }}>
      <AppBar position="fixed" color="inherit" elevation={0} sx={{ borderBottom: 1, borderColor: 'divider', ml: desktop ? `${width}px` : 0, width: desktop ? `calc(100% - ${width}px)` : '100%' }}>
        <Toolbar>
          {!desktop && <IconButton onClick={() => setOpen(true)}><Menu /></IconButton>}
          <Typography variant="h6" sx={{ ml: 1 }}>Smart Vehicle Park Booking System</Typography>
        </Toolbar>
      </AppBar>
      <Drawer variant={desktop ? 'permanent' : 'temporary'} open={desktop || open} onClose={() => setOpen(false)}>{drawer}</Drawer>
      <Box component="main" sx={{ flex: 1, p: { xs: 2, md: 3 }, mt: 8, ml: desktop ? `${width}px` : 0 }}>
        <Outlet />
      </Box>
    </Box>
  );
}
