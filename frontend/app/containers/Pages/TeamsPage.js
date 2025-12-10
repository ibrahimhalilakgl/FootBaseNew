import React, { useState, useEffect } from 'react';
import {
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  TextField,
  Box,
  Chip,
  Avatar,
} from '@mui/material';
import { teamsAPI } from 'utils/api';
import PapperBlock from 'dan-components/PapperBlock/PapperBlock';

function TeamsPage() {
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    loadTeams();
  }, []);

  const loadTeams = async () => {
    try {
      setLoading(true);
      const data = await teamsAPI.list();
      setTeams(data);
    } catch (error) {
      console.error('Takımlar yüklenemedi:', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredTeams = teams.filter(team =>
    team.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    (team.code && team.code.toLowerCase().includes(searchTerm.toLowerCase()))
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
      <PapperBlock title="Takımlar" icon="ios-people" desc="Tüm takımları görüntüleyin">
        <Box mb={3}>
          <TextField
            fullWidth
            label="Takım Ara"
            variant="outlined"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Takım adı veya kod ile ara..."
          />
        </Box>
        <Grid container spacing={3}>
          {filteredTeams.map((team) => (
            <Grid item xs={12} sm={6} md={4} key={team.id}>
              <Card>
                <CardContent>
                  <Box display="flex" alignItems="center" mb={2}>
                    <Avatar
                      sx={{ width: 56, height: 56, mr: 2, bgcolor: 'primary.main' }}
                    >
                      {team.code || team.name.charAt(0)}
                    </Avatar>
                    <Box>
                      <Typography variant="h6">
                        {team.name}
                      </Typography>
                      {team.code && (
                        <Chip label={team.code} size="small" color="primary" />
                      )}
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
        {filteredTeams.length === 0 && (
          <Box textAlign="center" py={4}>
            <Typography variant="h6" color="textSecondary">
              Takım bulunamadı
            </Typography>
          </Box>
        )}
      </PapperBlock>
    </Container>
  );
}

export default TeamsPage;

