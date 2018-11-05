import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Button from '@material-ui/core/Button';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import NotificationsIcon from '@material-ui/icons/Notifications';
import AddIcon from '@material-ui/icons/Add';
import EditIcon from '@material-ui/icons/Edit';
import Icon from '@material-ui/core/Icon';
import DeleteIcon from '@material-ui/icons/Delete';
import SearchIcon from '@material-ui/icons/Search';
import InputBase from '@material-ui/core/InputBase';
import ProjectList from './ProjectList'
import SimilarityChart from './Graphic'
import NewProjectDialog from './NewProjectDialog'



const drawerWidth = 300;

const styles = theme => ({
  root: {
    display: 'flex',
  },
  toolbar: {
    paddingRight: 24, // keep right padding when drawer closed
  },
  toolbarIcon: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    ...theme.mixins.toolbar,
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginLeft: 12,
    marginRight: 36,
  },
  menuButtonHidden: {
    display: 'none',
  },
  title: {
    flexGrow: 1,
  },
  drawerPaper: {
    position: 'relative',
    whiteSpace: 'nowrap',
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerPaperClose: {
    overflowX: 'hidden',
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    width: theme.spacing.unit * 7,
    [theme.breakpoints.up('sm')]: {
      width: theme.spacing.unit * 9,
    },
  },
  appBarSpacer: theme.mixins.toolbar,
  content: {
    flexGrow: 1,
    padding: theme.spacing.unit * 3,
    height: '100vh',
    overflow: 'auto',
  },
  chartContainer: {
    marginLeft: -22,
  },
  tableContainer: {
    height: 320,
  },
  h5: {
    marginBottom: theme.spacing.unit * 2,
  },
});


class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isShow: false,
      open: true,
      openNewDialog: false,
      projects:[],
      projectsNumber : 0
    };

    this.handleNewProject = this.handleNewProject.bind(this);
  }


  showTime = () => {
    this.setState({ isShow: true })
  }

  handleDrawerOpen = () => {
    this.setState({ open: true });
  };

  handleDrawerClose = () => {
    this.setState({ open: false });
  }

  setProjectChangeListener = (listener) => {
    this.projectChangeListener = listener;
  }

  handleProjectSelectionChange = (event, selectedProject) => {
    if (this.projectChangeListener) {
      this.projectChangeListener(selectedProject);
    }
  }

  handleNewProject() {
    this.setState({
      openNewDialog: true
    });
  }

  toggleNewProject = () => {
    
    this.setState({
      openNewDialog: !this.state.openNewDialog,
      projectsNumber : this.state.projectsNumber + 1 
    });
  }

  render() {
    const isShow = this.state.isShow;
    const { classes } = this.props;
    console.log('render state');
    if (!isShow) {
      return (
        <div className="App">

          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <p>
              Add <code>Project</code> and Scan to Rock.
              </p>

            <Button className="App-link" variant="contained" color="primary" onClick={this.showTime}>
              Rock Now
                </Button>
          </header>
        </div>
      );
    } else {
      return (<React.Fragment>
        <CssBaseline />
        <div className={classes.root}>
          <AppBar
            position="absolute"
            className={classNames(classes.appBar, this.state.open && classes.appBarShift)}
          >
            <Toolbar disableGutters={!this.state.open} className={classes.toolbar}>

              <IconButton
                color="inherit"
                aria-label="Open drawer"
                onClick={this.handleDrawerOpen}
                className={classNames(
                  classes.menuButton,
                  this.state.open && classes.menuButtonHidden,
                )}
              >
                <MenuIcon />
              </IconButton>
              <Typography
                component="h1"
                variant="h6"
                color="inherit"
                noWrap
                className={classes.title}
              >
                The Matrix
              </Typography>
              <div className={classes.search}>
                <div className={classes.searchIcon}>
                  <SearchIcon />
                </div>
                <InputBase
                  placeholder="Search…"
                  classes={{
                    root: classes.inputRoot,
                    input: classes.inputInput,
                  }}
                />
              </div>
              {/* <IconButton color="inherit">
                <Badge badgeContent={4} color="secondary">
                  <NotificationsIcon />
                </Badge>
              </IconButton> */}
            </Toolbar>
            <div>

            </div>
          </AppBar>
          <Drawer
            variant="permanent"
            classes={{
              paper: classNames(classes.drawerPaper, !this.state.open && classes.drawerPaperClose),
            }}
            open={this.state.open}
          >
            <div className={classes.toolbarIcon}>
              <div>
                <Button aria-label="Add" onClick={this.handleNewProject}>
                  <AddIcon />
                </Button>
                <Button aria-label="Edit" className={classes.button}>
                  <EditIcon />
                </Button>
                <Button disabled aria-label="Delete" className={classes.button}>
                  <DeleteIcon />
                </Button>
              </div>
              <IconButton onClick={this.handleDrawerClose}>
                <ChevronLeftIcon />
              </IconButton>

            </div>
            <Divider />

            <Divider />
            <ProjectList onSelectedChanged={this.handleProjectSelectionChange} expectedProjectNumber={this.state.projectsNumber}/>
          </Drawer>
          <main className={classes.content}>
            <div className={classes.appBarSpacer} />
            <Typography component="div" className={classes.chartContainer}>
              <SimilarityChart listToProjectEvent={this.setProjectChangeListener}/>
            </Typography>
          </main>
        </div>

        <NewProjectDialog open={this.state.openNewDialog} onClose={this.toggleNewProject} 
          fullScreen={true}/>
      </React.Fragment>);
    }
  }
}
App.propTypes = {
  classes: PropTypes.object.isRequired,
};
export default withStyles(styles)(App);
