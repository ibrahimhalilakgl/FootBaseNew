import React, { useState, useEffect } from 'react';
import {
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  TextField,
  Box,
  Rating,
  Chip,
} from '@mui/material';
import { playersAPI } from 'utils/api';
import PapperBlock from 'dan-components/PapperBlock/PapperBlock';

function PlayersPage() {
  const [players, setPlayers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    loadPlayers();
  }, []);

  const loadPlayers = async () => {
    try {
      setLoading(true);
      const data = await playersAPI.list();
      setPlayers(data);
    } catch (error) {
      console.error('Oyuncular yüklenemedi:', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredPlayers = players.filter(player =>
    player.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    (player.team && player.team.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  if (loading) {
    return (
      <Container>
        <Typography>Yükleniyor...</Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <PapperBlock title="Oyuncular" icon="ios-people" desc="Tüm oyuncuları görüntüleyin ve puanlayın">
        <Box mb={3}>
          <TextField
            fullWidth
            label="Oyuncu Ara"
            variant="outlined"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Oyuncu veya takım adı ile ara..."
          />
        </Box>
        <Grid container spacing={3}>
          {filteredPlayers.map((player) => (
            <Grid item xs={12} sm={6} md={4} key={player.id}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    {player.fullName}
                  </Typography>
                  {player.team && (
                    <Chip label={player.team} size="small" color="primary" sx={{ mb: 1 }} />
                  )}
                  {player.position && (
                    <Typography variant="body2" color="textSecondary" gutterBottom>
                      Pozisyon: {player.position}
                    </Typography>
                  )}
                  {player.shirtNumber && (
                    <Typography variant="body2" color="textSecondary" gutterBottom>
                      Forma No: {player.shirtNumber}
                    </Typography>
                  )}
                  {player.averageRating !== undefined && (
                    <Box mt={2}>
                      <Typography variant="body2" gutterBottom>
                        Ortalama Puan:
                      </Typography>
                      <Rating value={player.averageRating} precision={0.1} readOnly />
                      <Typography variant="caption" color="textSecondary">
                        ({player.averageRating.toFixed(1)}) - {player.ratingCount || 0} değerlendirme
                      </Typography>
                    </Box>
                  )}
                  <Box mt={2}>
                    <Button
                      variant="contained"
                      color="primary"
                      fullWidth
                      href={`/app/players/${player.id}`}
                    >
                      Detayları Gör
                    </Button>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
        {filteredPlayers.length === 0 && (
          <Box textAlign="center" py={4}>
            <Typography variant="h6" color="textSecondary">
              Oyuncu bulunamadı
            </Typography>
          </Box>
        )}
      </PapperBlock>
    </Container>
  );
}

export default PlayersPage;
