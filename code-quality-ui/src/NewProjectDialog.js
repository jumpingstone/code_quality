import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import TextField from '@material-ui/core/TextField';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import LocalProjectIcon from '@material-ui/icons/LocalActivity';
import GitIcon from '@material-ui/icons/SettingsRemote';
import withMobileDialog from '@material-ui/core/withMobileDialog';
import Typography from '@material-ui/core/Typography';

import { API_URL } from './Util';
import { postData } from './Util';

function TabContainer(props) {
  return (
    <Typography component="div" style={{ padding: 8 * 3 }}>
      {props.children}
    </Typography>
  );
}
function getLastName(gitRepo) {
  var name = gitRepo;
  var index = gitRepo.lastIndexOf('/');
  if (index > 0) {
    name = gitRepo.substring(index + 1);
  }
  return name;
}

class NewProjectDialog extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      value: 0,
      open: props.open,
      project: {
        name: '',
        path: '',
        gitRepo: ''
      }
    }
  }

  handleClickOpen = () => {
    this.setState({ open: true });
  };

  handleClose = () => {
    this.setState({ open: false });
    this.props.onClose();
  }

  setProjectName = (event) => {
    this.state.projec.name = event.target.value;
  }
  setProjectPath = (event) => {
    this.state.project.path = event.target.value;
  }
  setGitRepo = (event) => {
    this.state.project.gitRepo = event.target.value;
  }

  handleCreate = () => {
    console.log("in create new project");
    const { value } = this.state;
    var url = API_URL + '/projects/create' + this.state.project.name;
    var data = { name: this.state.project.name, path: this.state.project.path };
    if (value = 1) {
      url = API_URL + '/projects/clone';
      var data = { URL: this.state.project.gitRepo, username:'', password:'' };
    }
    postData(url, data)
      .then(response => {
        if (!response.ok) { throw response }
        return response.json()
      })
      .then(data => {
        this.setState({
          project: data
        });
      }) // JSON-string from `response.json()` call
      .catch(error => console.error(error));

    this.handleClose();
  }

  handleTabChange = (event, value) => {
    this.setState({ value });
  };

  render() {
    this.state.open = this.props.open;

    const { value } = this.state;
    return (
      <div>
        <Dialog
          open={this.state.open}
          onClose={this.handleClose}
          aria-labelledby="responsive-dialog-title"
        >
          <DialogTitle id="responsive-dialog-title">Create a new Project to Scan?</DialogTitle>
          <DialogContent>
            <DialogContentText>
              Input necessary content.
            </DialogContentText>
            <Tabs
              value={this.state.value}
              onChange={this.handleTabChange}
              fullWidth
              indicatorColor="secondary"
              textColor="secondary"
            >
              <Tab icon={<LocalProjectIcon />} label="Local">
              </Tab>
              <Tab icon={<GitIcon />} label="Git" />
            </Tabs>

            {value === 0 && <React.Fragment><TextField
              autoFocus
              margin="dense"
              id="name"
              label="Project Name"
              type="text"
              value={this.state.project.name}
              onChange={this.setProjectName}
              fullWidth
            />
              <TextField
                autoFocus
                margin="dense"
                id="name"
                label="Project Path"
                type="path"
                value={this.state.project.path}
                onChange={this.setProjectPath}
                fullWidth
              /></React.Fragment>}
            {value === 1 && <React.Fragment><TextField
              autoFocus
              margin="dense"
              id="gitRepo"
              label="Git Repository URL"
              type="text"
              value={this.state.project.gitRepo}
              onChange={this.getGitRepo}
              fullWidth
            /></React.Fragment>}

          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose} color="primary">
              Cancel
            </Button>
            <Button onClick={this.handleCreate} color="primary">
              Create
            </Button>
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}

export default withMobileDialog()(NewProjectDialog);