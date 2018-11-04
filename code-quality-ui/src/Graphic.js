import React, { Component } from 'react'
import './App.css'
import { Graph } from 'react-d3-graph';
import StepSlider from './StepSlider'

import { API_URL } from './Util';

const myConfig = {
    nodeHighlightBehavior: true,
    node: {
        labelProperty : 'label',
        fontSize : 14
    },
    link: {
        highlightColor: 'lightblue'
    }
};

class SimilarityChart extends Component {
    constructor(props) {
        super(props)
        this.state = {
            nodes: [],
            links: [],
            similarityLevel : 50
        }
        var self = this;
        this.props.listToProjectEvent((project) => {
            self.updateProject(project);
        });
    }
    componentDidMount() {
    }
    componentDidUpdate() {
    }

    changeSimilarityLevel = (event, level) => {
        this.updateProject(this.state.project, level);
    }

    updateProject(project, newSimilarityLevel) {
        console.log("in showResult");
        var level = newSimilarityLevel? newSimilarityLevel : this.state.similarityLevel ;
        var slevel = level * 1.0 / 100;

        fetch(API_URL + '/similarity/' + project.name + '/' + slevel)
            .then(response => {
                if (!response.ok) { throw response }
                return response.json()
            })
            .then(data => {
                this.setState({
                    project: project,
                    similarFiles: data.similarityFiles,
                    similarityLevel : level 
                });
            }) // JSON-string from `response.json()` call
            .catch(error => console.error(error));
    }

    truncateProjectPath = (path) => {
        var startIndex = path.indexOf(this.state.project.path);
        if (startIndex > 0) {
            return path.substring(startIndex);
        }
        return path;
    }

    
    getNodeLabel = (file) => {
        var label = file;
        var index = file.lastIndexOf('/');
        if (index > 0) {
            label = file.substring(index + 1);
        } else {
            index = file.lastIndexOf('\\');
            label = file.substring(index + 1);
        }
        return label;
    }

    onMouseOverLink = (source, target) => {
        window.alert(`Mouse over in link between ${source} and ${target}`);
    }

    render() {
        var nodes = [];
        var links = [];

        if (this.state.similarFiles) {
            this.state.similarFiles.map((node, i) => {
                var file = this.truncateProjectPath(node.file)
                var peer = this.truncateProjectPath(node.peer)

                var existingNode = nodes.find(e => e.id === file);
                if (existingNode) {
                    existingNode.connections = existingNode.connections + 1;
                    if (node.value > existingNode.similarity) {
                        existingNode.similarity = node.value;
                    }
                } else {
                    var label = this.getNodeLabel(file);
                    nodes.push({
                        id: file, label: label,
                        size: node.fileSize, connections: 1, similarity: node.value
                    });
                }
                var existingPeer = nodes.find(e => e.id === peer);
                if (existingPeer) {
                    existingPeer.connections = existingPeer.connections + 1;
                    if (node.value > existingPeer.similarity) {
                        existingPeer.similarity = node.value;
                    }
                } else {
                    var label = this.getNodeLabel(peer);
                    nodes.push({ id: peer, label: label,
                         size: node.peerSize, connections: 1, similarity: node.value });
                }

                links.push({ source: file, target: peer });
            });

            nodes.forEach(n => {
                // if (n.similarity > 0.7) {
                    var ints = Math.floor(n.similarity * 255)
                    n.color = '#f442' + ints.toString(16);
                // }
                n.size = n.size / 10 + 10;
            })
        }
        var data = { nodes: nodes, links: links };
        if (nodes.length) {
            return (
                <div>
                    <h3>Similarity: {this.state.similarityLevel}%</h3>
                    <StepSlider value={this.state.similarityLevel} onValueChanged={this.changeSimilarityLevel}/>
                    <Graph
                        id="file-similarity-graph-id" // id is mandatory, if no id is defined rd3g will throw an error
                        data={data}
                        config={myConfig}
                        onMouseOverLink={this.onMouseOverLink}
                    />
                </div>
            );
        } else {
            return "Not Ready";
        }
    }
}
export default SimilarityChart