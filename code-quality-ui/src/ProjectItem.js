import React, { Component } from 'react';

import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import LinearProgress from '@material-ui/core/LinearProgress';

import ScanIcon from '@material-ui/icons/PlayCircleFilled';

import IconButton from '@material-ui/core/IconButton';
import DashboardIcon from '@material-ui/icons/Dashboard';
import CancelIcon from '@material-ui/icons/Cancel'


import { API_URL } from './Util';
import { postData } from './Util';

class ProjectItem extends Component {

    constructor(props) {
        super(props);

        this.state = {
            project: this.props.project
        };
        this.doScan = this.doScan.bind(this);
        this.cancelScan = this.cancelScan.bind(this);
    }

    doScan() {
        console.log("in do scan");
        postData(API_URL + '/projects/scan/' + this.state.project.name)
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
    }

    cancelScan() {

    }

    handleListItemClick = (event) => {
        if (this.props.onSelected) {
            this.props.onSelected(event, this.state.project);
        }
    };

    render() {
        let actionIcon;
        let actionText;
        let actionHandler;

        actionHandler = this.doScan
        actionText = 'Scan';
        actionIcon = <ScanIcon />

        if (this.state.project.status === 'OnProgress') {
            actionText = 'Cancel'
            actionIcon = <CancelIcon />
            actionHandler = this.cancelScan
        }

        return (
            <ListItem button
                selected={this.props.selected}
                onClick={event => this.handleListItemClick(event)}>

                <ListItemIcon>
                    <DashboardIcon />
                </ListItemIcon>
                <ListItemText primary={this.state.project.name} />

                <IconButton aria-label={actionText} onClick={actionHandler}>
                    {actionIcon}
                </IconButton>


            </ListItem>
        );
    }
}


export default ProjectItem;