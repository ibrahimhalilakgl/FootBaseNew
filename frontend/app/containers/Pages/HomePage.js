import React, { useEffect, useState } from 'react';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import CircularProgress from '@mui/material/CircularProgress';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import { homeAPI } from 'utils/api';

function HomePage() {
  const [players, setPlayers] = useState([]);
  const [matches, setMatches] = useState([]);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let mounted = true;
    async function load() {
      try {
        const data = await homeAPI.get();
        if (!mounted) return;
        setPlayers(data.players || []);
        setMatches(data.matches || []);
        setComments(data.comments || []);
      } catch (err) {
        if (mounted) setError('Veri yüklenemedi. Lütfen tekrar deneyin.');
      } finally {
        if (mounted) setLoading(false);
      }
    }
    load();
    return () => {
      mounted = false;
    };
  }, []);

  const section = (title, content) => (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          {title}
        </Typography>
        {content}
      </CardContent>
    </Card>
  );

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" mt={4}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box mt={2}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  return (
    <Box mt={2}>
      <Typography variant="h5" gutterBottom>
        Ana Sayfa
      </Typography>
      <Grid container spacing={2}>
        <Grid item xs={12} md={4}>
          {section(
            'Son Eklenen Oyuncular',
            <List dense>
              {players.map((p) => (
                <React.Fragment key={p.id}>
                  <ListItem>
                    <ListItemText
                      primary={p.fullName || p.name}
                      secondary={`ID: ${p.id}${p.position ? ` • ${p.position}` : ''}`}
                    />
                  </ListItem>
                  <Divider component="li" />
                </React.Fragment>
              ))}
              {players.length === 0 && (
                <ListItem>
                  <ListItemText primary="Oyuncu bulunamadı" />
                </ListItem>
              )}
            </List>,
          )}
        </Grid>
        <Grid item xs={12} md={4}>
          {section(
            'Son Eklenen Maçlar',
            <List dense>
              {matches.map((m) => (
                <React.Fragment key={m.id}>
                  <ListItem>
                    <ListItemText
                      primary={`${m.homeTeam || '-'} vs ${m.awayTeam || '-'}`}
                      secondary={`ID: ${m.id}${m.status ? ` • ${m.status}` : ''}`}
                    />
                  </ListItem>
                  <Divider component="li" />
                </React.Fragment>
              ))}
              {matches.length === 0 && (
                <ListItem>
                  <ListItemText primary="Maç bulunamadı" />
                </ListItem>
              )}
            </List>,
          )}
        </Grid>
        <Grid item xs={12} md={4}>
          {section(
            'Son Yorumlar',
            <List dense>
              {comments.map((c, idx) => (
                <React.Fragment key={`${c.id || idx}`}>
                  <ListItem>
                    <ListItemText
                      primary={c.message || 'Yeni yorum'}
                      secondary={`ID: ${c.id || '-'}${c.author ? ` • ${c.author}` : ''}${c.createdAt ? ` • ${new Date(c.createdAt).toLocaleString('tr-TR')}` : ''}`}
                    />
                  </ListItem>
                  <Divider component="li" />
                </React.Fragment>
              ))}
              {comments.length === 0 && (
                <ListItem>
                  <ListItemText primary="Aktivite bulunamadı" />
                </ListItem>
              )}
            </List>,
          )}
        </Grid>
      </Grid>
    </Box>
  );
}

export default HomePage;

