import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';

function UserMenu() {
  const navigate = useNavigate();
  const [isAuthed, setIsAuthed] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    setIsAuthed(Boolean(token));
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    setIsAuthed(false);
    navigate('/login');
  };

  if (isAuthed) {
    return (
      <Stack direction="row" spacing={1} alignItems="center">
        <Button variant="outlined" color="primary" size="small" onClick={handleLogout}>
          Çıkış Yap
        </Button>
      </Stack>
    );
  }

  return (
    <Stack direction="row" spacing={1}>
      <Button component={Link} to="/login" variant="contained" color="primary" size="medium">
        Giriş Yap
      </Button>
      <Button component={Link} to="/register" variant="outlined" color="primary" size="medium">
        Kayıt Ol
      </Button>
    </Stack>
  );
}

export default UserMenu;
